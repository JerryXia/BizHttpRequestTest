'use strict';

const index = {
    template: '#index',
    data: function () {
        return {
            libInfo: { serverOsName: '', javaClassPath: '' },
            memoryMXBean: {
                heapMemoryUsag: '',
                nonHeapMemoryUsag: ''
            },
            memoryPoolMXBeans: [],
            requestAttributes: {},
            serverstat: {}
        }
    },
    created: function () {
        console.info('index created');
        this.fetchData();
    },
    updated: function () {
        //console.info('index updated');
    },
    destroyed: function () {
        console.info('index destroyed');
    },
    watch: {
        '$route': 'fetchData',
        'serverstat': function (newVal, oldVal) {
            let htmlPartial = [
                '<li><a href="https://github.com/JerryXia/BizHttpRequestTest" target="_blank">devHelper.ReqeustCapture</a></li>',
                '<li>MemoryStorage</li>',
                '<li>Server Time: ',
                new Date(newVal.time).format('yyyy-MM-dd HH:mm:ss'),
                '</li>',
                '<li>Generated: ',
                newVal.generated,
                'ns</li>'
            ];
            $('.footer ul').html(htmlPartial.join('')).show();
        }
    },
    computed: {

    },
    methods: {
        fetchData: function () {
            let that = this;

            jQuery.getJSON(PATH_PREFIX + '/snoop.json?callback=?', {}, function(res) {
                if(res && res.code === 1) {
                    that.libInfo = res.data.libInfo;
                    that.memoryMXBean = res.data.memoryMXBean;
                    that.memoryPoolMXBeans = res.data.memoryPoolMXBeans;
                    that.requestAttributes = that.objectNameOrderedKeyValue(res.data.requestAttributes);
                    
                    that.serverstat = res.serverstat;
                } else {
                    
                }
            });
        },
        objectNameOrderedKeyValue: function(obj) {
            let kvArray = [];
            for(let k in obj) {
                kvArray.push({ key: k, value: obj[k] });
            }
            return _.sortBy(kvArray, function(item){ return item.key; });
        }
    }
};

const apiRecords = {
    template: '#apiRecords',
    data: function () {
        return {
            apiRecordsPagedList: [],
            logTbShow: {
                id: false,
                timeStamp: true,
                method: true,
                requestAndQuery: true,
                parameter: true,
                payload: false
            },
            apiRecordLogsQueryId: '',
            apiRecordLogs: [],
            serverstat: {},
            isFetchingData: false,
            fetchedTick: 5,
            fetchedTickInterval: null,
            currFetchHasNew: false,
            lastRecordIndex: 0,
            pageSize: 10,
            selectedFilterUrl: '',
            allFilterUrls: [],
            queryLevels: 'WARN,ERROR,FATAL',
            exceptionRecords: {},
            isFetchingExceptionRecords: false,
            fetchExceptionRecordsTimeout: null
        }
    },
    route: {
        data: function (transition) {
            //transition.next({
            //    currentPath: transition.to.path
            //})
        }
    },
    created: function () {
        console.log('apiRecords created');
        this.loadFromLocalDb();
        this.fetchData();
        this.fetchExceptionRecords();
    },
    updated: function () {
        //console.debug('apiRecords updated');
    },
    destroyed: function () {
        let that = this;
        console.log('apiRecords destroyed');
        if (that.fetchedTickInterval != null) {
            clearInterval(that.fetchedTickInterval);
            that.fetchedTickInterval = null;
        }
        if(that.fetchExceptionRecordsTimeout != null){
            clearTimeout(that.fetchExceptionRecordsTimeout);
            that.fetchExceptionRecordsTimeout = null;
        }
    },
    watch: {
        '$route': 'fetchData',
        'logTbShow': {
            handler: function (val, oldVal) {
                this.saveLogTbShowConfigToLocalDb(val);
            },
            deep: true
        },
        'serverstat': function (newVal, oldVal) {
            let htmlPartial = [
                '<li><a href="https://github.com/JerryXia/BizHttpRequestTest" target="_blank">devHelper.ReqeustCapture</a></li>',
                '<li>MemoryStorage</li>',
                '<li>Server Time: ',
                new Date(newVal.time).format('yyyy-MM-dd HH:mm:ss'),
                '</li>',
                '<li>Generated: ',
                newVal.generated,
                'ns</li>'
            ];
            $('.footer ul').html(htmlPartial.join('')).show();
        },
        'isFetchingData': function (newVal, oldVal) {
            let that = this;
            if (oldVal === false && newVal === true) {
                // 开始获取
                if (that.fetchedTickInterval != null) {
                    clearInterval(that.fetchedTickInterval);
                    that.fetchedTickInterval = null;
                }
                $('#btn_loadmore').attr('disabled', 'disabled');
            } else if (oldVal === true && newVal === false) {
                // 获取结束
                if (that.fetchedTickInterval == null) {
                    that.fetchedTick = 5;
                    that.fetchedTickInterval = setInterval(function () {
                        let newFetchedTick = that.fetchedTick - 1;
                        if (newFetchedTick > 0) {
                            that.fetchedTick = newFetchedTick;
                        } else {
                            that.fetchNextData();
                        }
                    }, 1000);
                }
                $('#btn_loadmore').removeAttr('disabled');
            } else {
                console.warn('watch isFetchingData into extra case.');
            }
        },
        'isFetchingExceptionRecords': function(newVal, oldVal) {
            let that = this;
            if (oldVal === false && newVal === true) {
                // 开始获取
            } else if (oldVal === true && newVal === false) {
                // 获取结束
                if (that.fetchExceptionRecordsTimeout != null) {
                    clearTimeout(that.fetchExceptionRecordsTimeout);
                }
                that.fetchExceptionRecordsTimeout = setTimeout(function () {
                    that.fetchExceptionRecords();
                }, 1000);
            } else {
                console.warn('watch isFetchingExceptionRecords into extra case.');
            }
        },
        'apiRecordsPagedList': function(newVal, oldVal){
            let that = this;
            let requestURIs = _.map(newVal, function(item){ return item.requestURI; })
            that.allFilterUrls = _.uniq(requestURIs);
        }
    },
    computed: {
        logCount: function () {
            let that = this;
            return 0;
        },
        fetchedMessage: function () {
            let that = this;
            return that.currFetchHasNew ? '' : '找不到与当前过滤条件相符的更新的记录。';
        },
        fetchedButtonValue: function () {
            let that = this;
            if (that.isFetchingData) {
                return ' loading...';
            } else {
                return '' + that.fetchedTick + '秒后自动加载较新记录';
            }
        },
        filteredApiRecordsPagedList: function() {
            let that = this;
            if(that.selectedFilterUrl === '') {
                return that.apiRecordsPagedList;
            } else {
                return _.filter(that.apiRecordsPagedList, function(item){ return item.requestURI == that.selectedFilterUrl });
            }
        }
    },
    methods: {
        loadFromLocalDb: function () {
            let that = this;
            if (window.localStorage) {
                try {
                    let jsonStr = localStorage.getItem("requestcapture_apirecords_logTbShow");
                    if(jsonStr && jsonStr.length > 0) {
                        let jsonObj = JSON.parse(jsonStr);
                        that.logTbShow = jsonObj;
                        console.info('requestcapture_apirecords_logTbShow:加载本地保存的配置');
                    }
                } catch(parseError) {
                    console.error(parseError);
                    console.info('requestcapture_apirecords_logTbShow:使用的默认配置');
                }

                try {
                    let jsonStr = localStorage.getItem("requestcapture_settingsShowExceptionRecords_levels");
                    if(jsonStr && jsonStr.length > 0) {
                        let jsonObj = JSON.parse(jsonStr);
                        that.queryLevels = jsonObj.join(',');
                        console.info('settingsShowExceptionRecords:加载本地保存的配置');
                    }
                } catch(parseError) {
                    console.error(parseError);
                    console.info('settingsShowExceptionRecords:使用的默认配置');
                }
            } else {
                alert(NOT_SUPPORT_LOCALSTORAGE);
            }
        },
        saveLogTbShowConfigToLocalDb: function (obj) {
            let that = this;
            if (window.localStorage) {
                window.localStorage.setItem("requestcapture_apirecords_logTbShow", JSON.stringify(obj));
            } else {
                alert(NOT_SUPPORT_LOCALSTORAGE);
            }
        },
        fetchData: function () {
            let that = this;

            that.isFetchingData = true;
            jQuery.ajax({
                type: 'GET',
                url: PATH_PREFIX + '/allapirecords.json',
                timeout: 123000,
                //contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                //data: { t: Date.now() },
                dataType: 'jsonp',
                success: function(res, textStatus, jqXHR) {
                    if (res && res.code == 1) {
                        that.lastRecordIndex = res.data.lastIndex;
                        that.apiRecordsPagedList = res.data.list;

                        that.serverstat = res.serverstat;
                        that.currFetchHasNew = res.data.list && res.data.list.length > 0;
                    } else {
                        that.apiRecordsPagedList = [];
                        that.currFetchHasNew = false;
                    }
                    //that.isFetchingData = false;
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    that.apiRecordsPagedList = [];
                    that.currFetchHasNew = false;
                },
                complete: function(jqXHR, textStatus) {
                    that.isFetchingData = false;
                    // "success", "notmodified", "nocontent", "error", "timeout", "abort", or "parsererror"
                    console.log('allapirecords.json:' + textStatus);
                }
            });
        },
        showLogs: function (recordId, event) {
            let that = this;
            var $btn = $(event.target).button('loading');

            that.apiRecordLogsQueryId = recordId;
            $.getJSON(PATH_PREFIX + '/apirecordlogs.json?callback=?', { id: recordId }, function (res) {
                if (res && res.code == 1) {
                    that.apiRecordLogs = res.data;
                } else {
                    that.apiRecordLogs = [];
                }
                $btn.button('reset');
                $('.bs-example-modal-lg').modal();
            });
        },
        replay: function (apiRecord, event) {
            let replayUrlHost = '';
            if (window.localStorage) {
                replayUrlHost = localStorage.getItem("rquestcapture:replayUrlHost");
            } else {
                alert(NOT_SUPPORT_LOCALSTORAGE);
            }
            if (replayUrlHost == null || replayUrlHost.length === 0) {
                alert('请先前往settings:replay配置host');
                return;
            }

            let that = this;
            let $btn = $(event.target).button('loading');
            $.ajax({
                method: apiRecord.method,
                url: replayUrlHost + apiRecord.requestURI + ((apiRecord.queryString && apiRecord.queryString.length>0) ? '?'+ apiRecord.queryString : ''),
                headers: that.filteredHeaders(apiRecord.headers),
                data: (apiRecord.contentType && apiRecord.contentType.indexOf('application/x-www-form-urlencoded;') === 0) ? that.parameterFormat(apiRecord.parameterMap) : (apiRecord.payload || ''),
                success: function (res) {
                    
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.info(jqXHR); 
                    console.info(textStatus); 
                    console.error(errorThrown);
                },
                complete: function (jqXHR, textStatus) {
                    $btn.button('reset');
                }
            });
        },
        filteredHeaders: function(headers) {
            let excludeHeaders = [ 'accept', 'accept-encoding', 'accept-language', 'connection', 'content-length', 'cookie', 'host', 'user-agent' ];
            let requestingHeaders = {};
            if (headers) {
                for (let k in headers) {
                    if(excludeHeaders.indexOf(k) > -1 || k.indexOf('x-') === 0) {
                        console.log('exclude ' + k);
                    } else {
                        let v = headers[k];
                        if (Array.isArray(v)) {
                            if (v.length === 1) {
                                requestingHeaders[k] = v[0];
                            } else {
                                requestingHeaders[k] = v;
                            }
                        } else {
                            requestingHeaders[k] = v;
                        }
                    }
                }
            }
            console.log(requestingHeaders);
            return requestingHeaders;
        },
        parameterFormat: function (obj) {
            let formaterObj = {};
            //obj = JSON.parse(JSON.stringify(obj));
            if (obj) {
                for (let k in obj) {
                    let v = obj[k];
                    if (Array.isArray(v)) {
                        if (v.length === 1) {
                            formaterObj[k] = v[0];
                        } else {
                            formaterObj[k] = v;
                        }
                    } else {
                        formaterObj[k] = v;
                    }
                }
            }
            return formaterObj;
        },
        fetchNextData: function () {
            let that = this;

            let start = that.lastRecordIndex;
            let end = that.lastRecordIndex + that.pageSize;

            that.isFetchingData = true;
            jQuery.ajax({
                type: 'GET',
                url: PATH_PREFIX + '/apirecords.json',
                timeout: 123000,
                //contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                data: { startIndex: start, endIndex: end },
                dataType: 'jsonp',
                success: function(res, textStatus, jqXHR) {
                    if (res && res.code == 1) {
                        that.lastRecordIndex = res.data.lastIndex;
                        if (res.data.list && res.data.list.length > 0) {
                            for (let i = 0, len = res.data.list.length; i < len; i++) {
                                that.apiRecordsPagedList.push(res.data.list[i]);
                            }
                        }

                        that.serverstat = res.serverstat;
                        that.currFetchHasNew = res.data.list && res.data.list.length > 0;
                    } else {
                        that.currFetchHasNew = false;
                    }
                    //that.isFetchingData = false;
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    that.currFetchHasNew = false;
                },
                complete: function(jqXHR, textStatus) {
                    that.isFetchingData = false;
                    // "success", "notmodified", "nocontent", "error", "timeout", "abort", or "parsererror"
                    console.log('apirecords.json:' + textStatus);
                }
            });
        },
        switchExpandMessage: function (item) {
            if (typeof item.isExpandMessage === 'undefined') {
                Vue.set(item, 'isExpandMessage', true);
            } else {
                item.isExpandMessage = item.isExpandMessage ? false : true;
            }
        },
        fetchExceptionRecords: function () {
            let that = this;

            that.isFetchingExceptionRecords = true;
            jQuery.ajax({
                type: 'GET',
                url: PATH_PREFIX + '/exceptionRecords.json',
                timeout: 123000,
                data: { levels: that.queryLevels },
                dataType: 'jsonp',
                success: function(res, textStatus, jqXHR) {
                    if (res && res.code == 1) {
                        // TODO: 优化clone
                        for(let i = 0, len = ORDERED_LEVELS.length; i < len; i ++) {
                            let level = ORDERED_LEVELS[i];
                            let recordIds = res.data[level];
                            if(recordIds && recordIds.length > 0) {
                                for(let j = 0, jlen = recordIds.length; j < jlen; j++) {
                                    let oldVal = that.exceptionRecords[recordIds[j]];
                                    if(oldVal) {
                                        if(ORDERED_LEVELS_RANK[level] > ORDERED_LEVELS_RANK[oldVal]){
                                            that.exceptionRecords[recordIds[j]] = level;
                                        }
                                    } else {
                                        Vue.set(that.exceptionRecords, recordIds[j], level);
                                    }
                                }
                            }
                        }
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {

                },
                complete: function(jqXHR, textStatus) {
                    that.isFetchingExceptionRecords = false;
                    // "success", "notmodified", "nocontent", "error", "timeout", "abort", or "parsererror"
                    console.log('exceptionRecords.json:' + textStatus);
                }
            });
        },
        apiRecordExceptionLogLevelClassFormater: function(value) {
            let val = '';
            if(typeof value === 'undefined'){
                value = '';
            }
            switch (value) {
                case 'FATAL':
                case 'ERROR':
                    val = 'alert-danger';
                    break;
                case 'WARN':
                    val = 'alert-warning';
                    break;
                case 'INFO':
                    val = 'alert-info';
                    break;
                case 'DEBUG':
                    val = 'alert-success';
                    break;
                case 'TRACE':
                default:
                    val = '';
                    break;
            }
            return val;
        }
    }
};
const apiLogs = {
    template: '#apiLogs',
    data: function () {
        return {
            currUrl: '',
            initHash: '',
            clipboard: null,
            isFetchingData: {
                apiRecord: false,
                apiRecordLogs: false
            },
            apiRecord: {},
            apiRecordLogs: [],
            logTbShow: {
                id: false,
                httpRequestRecordId: false,
                timeStamp: true,
                level: true,
                threadName: true,
                loggerName: true,
                message: true,
                host: false,
                ip: false
            }
        }
    },
    route: {
        data: function (transition) {
            //transition.next({
            //    currentPath: transition.to.path
            //})
        }
    },
    created: function () {
        let that = this;
        console.log('apiLogs created');
        that.fetchData();
        that.currUrl = window.location.href;
        that.initHash = location.hash;
        let clipboard = new Clipboard('.btn');
        clipboard.on('success', function(e) {
            e.clearSelection();
            window.setTimeout(function(){ 
                $('[data-toggle="tooltip"]').tooltip('hide');
            }, 1000);
        });
        that.clipboard = clipboard;
    },
    mounted: function () {
        this.$nextTick(function () {
            // Code that will run only after the entire view has been rendered
            $('body').css({ 'padding-top': '0px' });
            $('nav').removeClass('navbar-fixed-top').addClass('navbar-static-top');
            $('#linkApiLogs').removeClass('none');
            $('[data-toggle="tooltip"]').tooltip();          
        });
    },
    updated: function () {
        //console.log('apiLogs updated');
    },
    destroyed: function () {
        console.log('apiLogs destroyed');
        this.clipboard.destroy();
        $('#linkApiLogs').addClass('none');
        $('[data-toggle="tooltip"]').tooltip('destroy');
    },
    watch: {
        'isFetchingData': {
            handler: function (val, oldVal) {
                let that = this;
                that.$nextTick(function () {
                    // 设置tr的id
                    $('table tr').each(function(i, v) {
                        let idAttr = v.getAttribute('id');
                        if(!(idAttr && idAttr.length > 0)) {
                            let $td0 = $(v).find('td').eq(0);
                            let td0text = $td0.text();
                            v.setAttribute('id', td0text);
                            $td0.html('<small><a href="#'+ td0text +'" style="color:#333;text-decoration:none;">'+ td0text +'</a></small>');
                        }
                    });
                    // dom渲染完毕后，定位目标行的位置，并高亮显示
                    if(val.apiRecord === false && val.apiRecordLogs === false) {
                        location.hash = '';
                        location.hash = that.initHash;
                        if(that.initHash && that.initHash.length > 0) {
                            let $elInitHash = $('' + that.initHash);
                            if($elInitHash.length > 0){
                                let showInfo = function() {
                                    $elInitHash.addClass('alert').addClass('alert-info');
                                };
                                let hideInfo = function() {
                                    $elInitHash.removeClass('alert').removeClass('alert-info');
                                };
                                $elInitHash.css({display: 'none'});
                                showInfo();
                                $elInitHash.fadeIn(3000, function() {
                                    setTimeout(hideInfo, 50);
                                    setTimeout(showInfo, 1050);
                                    setTimeout(hideInfo, 2050);
                                    setTimeout(showInfo, 3050);
                                    setTimeout(hideInfo, 4050);
                                });
                            }
                        }
                    }
                });
            },
            deep: true
        }
    },
    methods: {
        fetchData: function() {
            let that = this;

            let queryId = that.$route.query.apiRecordId;
            if(queryId && queryId.length > 0) {
                that.queryApiRecord(queryId);
                that.queryApiRecordLogs(queryId);
            } else {
                //TODO: empty result
            }
        },
        queryApiRecord: function(apiRecordId) {
            let that = this;
            that.isFetchingData.apiRecord = true;
            $.getJSON('apirecord.json?callback=?', { id: apiRecordId }, function (res) {
                if (res && res.code == 1 && res.data.length > 0) {
                    that.apiRecord = res.data[0];
                } else {
                    that.apiRecord = {};
                }
                that.isFetchingData.apiRecord = false;
            });
        },
        queryApiRecordLogs: function(apiRecordId) {
            let that = this;
            that.isFetchingData.apiRecordLogs = true;
            $.getJSON('apirecordlogs.json?callback=?', { id: apiRecordId }, function (res) {
                if (res && res.code == 1) {
                    that.apiRecordLogs = res.data;
                } else {
                    that.apiRecordLogs = [];
                }
                that.isFetchingData.apiRecordLogs = false;
            });
        }
    }
};
const allLogs = {
    template: '#allLogs',
    data: function () {
        return {
            logLevels: ['ALL', 'TRACE', 'DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL'],
            queryLevel: 'ALL',
            queryFrom: 0,
            queryCount: 16,
            logPagedList: [],
            logTbShow: {
                id: false,
                httpRequestRecordId: false,
                timeStamp: true,
                level: true,
                threadName: true,
                loggerName: true,
                message: true,
                host: false,
                ip: false
            },
            serverstat: {},
            isFirstFetching: true,
            isFetchingData: false,
            fetchedTickTimeout: null,
            currFetchHasNew: false,
            lastRecordIndex: 0,
            pageSize: 16
        }
    },
    created: function () {
        console.log('allLogs created');
        this.fetchData();
        this.filterLogs();
    },
    updated: function () {
        //console.log('allLogs updated');
    },
    destroyed: function () {
        let that = this;
        console.log('allLogs destroyed');
        if (that.fetchedTickTimeout != null) {
            clearTimeout(that.fetchedTickTimeout);
            that.fetchedTickTimeout = null;
        }
    },
    watch: {
        '$route': 'filterLogs',
        'serverstat': function (newVal, oldVal) {
            let htmlPartial = [
                '<li><a href="https://github.com/JerryXia/BizHttpRequestTest" target="_blank">devHelper.ReqeustCapture</a></li>',
                '<li>MemoryStorage</li>',
                '<li>Server Time: ',
                new Date(newVal.time).format('yyyy-MM-dd HH:mm:ss'),
                '</li>',
                '<li>Generated: ',
                newVal.generated,
                'ns</li>'
            ];
            $('.footer ul').html(htmlPartial.join('')).show();
        },
        'isFetchingData': function (newVal, oldVal) {
            let that = this;
            if (oldVal === false && newVal === true) {
                // 开始获取
                if (that.fetchedTickTimeout != null) {
                    clearTimeout(that.fetchedTickTimeout);
                    that.fetchedTickTimeout = null;
                }
                //$('#btn_loadmore').attr('disabled', 'disabled');
            } else if (oldVal === true && newVal === false) {
                // 获取结束
                if (that.fetchedTickTimeout == null) {
                    that.fetchedTickTimeout = setTimeout(function () {
                        that.fetchNextData();
                    }, 1000);
                }
                //$('#btn_loadmore').removeAttr('disabled');
            } else {
                console.log('watch isFetchingData into extra case.');
            }
        }
    },
    computed: {
        logCount: function () {
            let that = this;
            return {
                ALL: that.logPagedList.length,
                TRACE: _.filter(that.logPagedList, function (logItem) { return logItem.level == 'TRACE'; }).length,
                DEBUG: _.filter(that.logPagedList, function (logItem) { return logItem.level == 'DEBUG'; }).length,
                INFO: _.filter(that.logPagedList, function (logItem) { return logItem.level == 'INFO'; }).length,
                WARN: _.filter(that.logPagedList, function (logItem) { return logItem.level == 'WARN'; }).length,
                ERROR: _.filter(that.logPagedList, function (logItem) { return logItem.level == 'ERROR'; }).length,
                FATAL: _.filter(that.logPagedList, function (logItem) { return logItem.level == 'FATAL'; }).length
            }
        },
        queryedLogPagedList: function() {
            let that = this;

            if (that.queryLevel === 'ALL') {
                return that.logPagedList.slice(that.queryFrom, that.queryFrom + that.queryCount);
            } else {
                return _.filter(that.logPagedList, function (logItem) {
                    return logItem.level == that.queryLevel;
                }).slice(that.queryFrom, that.queryFrom + that.queryCount);
            }
        },
        lastPageStartIndex: function() {
            let that = this;
            
            //let currLevelLogCount = 0;
            //if (that.queryLevel === 'ALL') {
            //    currLevelLogCount = that.logPagedList.length;
            //} else {
            //    currLevelLogCount = _.filter(that.logPagedList, function (logItem) { return logItem.level == that.queryLevel; }).length;
            //}
            let currLevelLogCount = that.logCount[that.queryLevel];
            return currLevelLogCount % that.queryCount == 0 ? currLevelLogCount - that.queryCount : Math.floor(currLevelLogCount / that.queryCount) * that.queryCount;
        }
    },
    methods: {
        fetchData: function () {
            let that = this;

            that.isFetchingData = true;
            jQuery.ajax({
                type: 'GET',
                url: PATH_PREFIX + '/alllogs.json',
                timeout: 123000,
                data: { },
                dataType: 'jsonp',
                success: function(res, textStatus, jqXHR) {
                    if (res && res.code == 1) {
                        that.lastRecordIndex = res.data.lastIndex;
                        that.logPagedList = res.data.list;
                        
                        that.serverstat = res.serverstat;
                        that.currFetchHasNew = res.data.list && res.data.list.length > 0;
                    } else {
                        that.logPagedList = [];
                        that.currFetchHasNew = false;
                    }
                    //that.isFetchingData = false;
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    that.currFetchHasNew = false;
                },
                complete: function(jqXHR, textStatus) {
                    that.isFetchingData = false;
                    that.isFirstFetching = false;
                    // "success", "notmodified", "nocontent", "error", "timeout", "abort", or "parsererror"
                    console.log('alllogs.json:' + textStatus);
                }
            });
        },
        fetchNextData: function () {
            let that = this;

            let start = that.lastRecordIndex;
            let end = that.lastRecordIndex + that.pageSize;

            that.isFetchingData = true;
            jQuery.ajax({
                type: 'GET',
                url: PATH_PREFIX + '/logs.json',
                timeout: 123000,
                data: { startIndex: start, endIndex: end },
                dataType: 'jsonp',
                success: function(res, textStatus, jqXHR) {
                    if (res && res.code == 1) {
                        that.lastRecordIndex = res.data.lastIndex;
                        if (res.data.list && res.data.list.length > 0) {
                            for (let i = 0, len = res.data.list.length; i < len; i++) {
                                that.logPagedList.push(res.data.list[i]);
                            }
                        }

                        that.serverstat = res.serverstat;
                        that.currFetchHasNew = res.data.list && res.data.list.length > 0;
                    } else {
                        that.currFetchHasNew = false;
                    }
                    //that.isFetchingData = false;
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    that.currFetchHasNew = false;
                },
                complete: function(jqXHR, textStatus) {
                    that.isFetchingData = false;
                    // "success", "notmodified", "nocontent", "error", "timeout", "abort", or "parsererror"
                    console.log('logs.json:' + textStatus);
                }
            });
        },
        filterLogs: function() {
            console.log('alllogs filterlogs');
            let that = this;

            let queryLevel = 'ALL';
            if (that.$route.params.level) {
                if (that.$route.params.level.length > 0) {
                    queryLevel = that.$route.params.level;
                }
            }
            if(that.queryLevel != queryLevel) {
                that.queryLevel = queryLevel;
            }

            let queryFrom = 0;
            if (that.$route.query.from) {
                queryFrom = typeof that.$route.query.from === 'string' ? parseInt(that.$route.query.from) : that.$route.query.from;
            }
            if(that.queryFrom != queryFrom) {
                that.queryFrom = queryFrom;
            }

            let queryCount = 16;
            if (that.$route.query.count) {
                queryCount = typeof that.$route.query.count === 'string' ? parseInt(that.$route.query.count) : that.$route.query.count;
            }
            if(that.queryCount != queryCount) {
                that.queryCount = queryCount;
            }
        },
        switchExpandMessage: function (item) {
            if (typeof item.isExpandMessage === 'undefined') {
                Vue.set(item, 'isExpandMessage', true);
            } else {
                item.isExpandMessage = item.isExpandMessage ? false : true;
            }
        }
    }
};
const settingsReplay = {
    template: '#settings_replay',
    data: function () {
        return {
            replayUrlHost: ''
        }
    },
    created: function () {
        this.loadFromLocalDb();

        Vue.nextTick(function () {
            $('#settings').addClass('active');
        });
    },
    destroyed: function () {
        $('#settings').removeClass('active');
    },
    methods: {
        loadFromLocalDb: function () {
            let that = this;
            if (window.localStorage) {
                that.replayUrlHost = localStorage.getItem("rquestcapture:replayUrlHost");
            } else {
                alert(NOT_SUPPORT_LOCALSTORAGE);
            }
        },
        saveToLocalDb: function () {
            let that = this;
            if (window.localStorage) {
                window.localStorage.setItem("rquestcapture:replayUrlHost", that.replayUrlHost);
            } else {
                alert(NOT_SUPPORT_LOCALSTORAGE);
            }
        }
    }
};
const settingsShowExceptionRecords = {
    template: '#settings_show_exception_records',
    data: function () {
        return {
            shownLevels: [ 'WARN', 'ERROR', 'FATAL' ]
        }
    },
    created: function () {
        this.loadFromLocalDb();

        Vue.nextTick(function () {
            $('#settings').addClass('active');
        });
    },
    destroyed: function () {
        $('#settings').removeClass('active');
    },
    watch: {
        'shownLevels': function (val, oldVal) {
            this.saveToLocalDb(val);
        }
    },
    methods: {
        loadFromLocalDb: function () {
            let that = this;
            try {
                let jsonStr = localStorage.getItem("requestcapture_settingsShowExceptionRecords_levels");
                if(jsonStr && jsonStr.length > 0) {
                    let jsonObj = JSON.parse(jsonStr);
                    that.shownLevels = jsonObj;
                    console.info('settingsShowExceptionRecords:加载本地保存的配置');
                }
            } catch(parseError) {
                console.error(parseError);
                console.info('settingsShowExceptionRecords:使用的默认配置');
            }
        },
        saveToLocalDb: function (obj) {
            let that = this;
            if (window.localStorage) {
                window.localStorage.setItem("requestcapture_settingsShowExceptionRecords_levels", JSON.stringify(obj));
            } else {
                alert(NOT_SUPPORT_LOCALSTORAGE);
            }
        }
    }
};

const NOT_SUPPORT_LOCALSTORAGE = 'This browser does not support localStorage';
const ORDERED_LEVELS = [ 'TRACE', 'DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL' ];
const ORDERED_LEVELS_RANK = { TRACE: 1, DEBUG: 2, INFO: 3, WARN: 4, ERROR: 5, FATAL: 6 };

const router = new VueRouter({
    mode: 'history',
    routes: [
        { path: PATH_PREFIX + '/apirecords.html', component: apiRecords },
        { path: PATH_PREFIX + '/apilogs.html', component: apiLogs },
        { path: PATH_PREFIX + '/alllogs/:level.html', component: allLogs },
        { path: PATH_PREFIX + '/settings/replay.html', component: settingsReplay },
        { path: PATH_PREFIX + '/settings/show_exception_records.html', component: settingsShowExceptionRecords },
        { path: PATH_PREFIX + '/index.html', component: index }
    ]
});
const app = new Vue({
    router: router
}).$mount('#wrap');

if (typeof Date.prototype.format == 'undefined') {
    Date.prototype.format = function (mask) {
        var d = this;
        var zeroize = function (value, length) {
            if (!length) length = 2;
            value = String(value);
            for (var i = 0, zeros = ''; i < (length - value.length); i++) {
                zeros += '0';
            }
            return zeros + value;
        };

        return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0) {
            switch ($0) {
                case 'd': return d.getDate();
                case 'dd': return zeroize(d.getDate());
                case 'ddd': return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
                case 'dddd': return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
                case 'M': return d.getMonth() + 1;
                case 'MM': return zeroize(d.getMonth() + 1);
                case 'MMM': return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
                case 'MMMM': return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
                case 'yy': return String(d.getFullYear()).substr(2);
                case 'yyyy': return d.getFullYear();
                case 'h': return d.getHours() % 12 || 12;
                case 'hh': return zeroize(d.getHours() % 12 || 12);
                case 'H': return d.getHours();
                case 'HH': return zeroize(d.getHours());
                case 'm': return d.getMinutes();
                case 'mm': return zeroize(d.getMinutes());
                case 's': return d.getSeconds();
                case 'ss': return zeroize(d.getSeconds());
                case 'l': return zeroize(d.getMilliseconds(), 3);
                case 'L': var m = d.getMilliseconds();
                    if (m > 99) m = Math.round(m / 10);
                    return zeroize(m);
                case 'tt': return d.getHours() < 12 ? 'am' : 'pm';
                case 'TT': return d.getHours() < 12 ? 'AM' : 'PM';
                case 'Z': return d.toUTCString().match(/[A-Z]+$/);
                // Return quoted strings with the surrounding quotes removed
                default: return $0.substr(1, $0.length - 2);
            }
        });
    };
};

Vue.filter('objectIdTimeStamp', function (value, formater) {
    return new Date(parseInt(value.substring(0, 8), 16) * 1000).format(formater);
});
Vue.filter('timeStamp', function (value, formater) {
    return new Date(value).format(formater);
});
Vue.filter('showRequestAndQuery', function (apiRecord) {
    let val = apiRecord.requestURI + ( (apiRecord.queryString && apiRecord.queryString.length>0) ? '?'+ apiRecord.queryString : '');
    return val;
});
Vue.filter('parameterFormater', function (obj) {
    let formaterObj = {};
    //obj = JSON.parse(JSON.stringify(obj));
    if (obj) {
        for (let k in obj) {
            let v = obj[k];
            if (Array.isArray(v)) {
                if (v.length === 1) {
                    formaterObj[k] = v[0];
                } else {
                    formaterObj[k] = v;
                }
            } else {
                formaterObj[k] = v;
            }
        }
    }
    return JSON.stringify(formaterObj);
});
Vue.filter('logLevelFormater', function (value) {
    let val = '';
    switch (value.toLowerCase()) {
        case 'fatal':
        case 'error':
            val = 'label-danger';
            break;
        case 'warn':
            val = 'label-warning';
            break;
        case 'info':
            val = 'label-info';
            break;
        case 'debug':
            val = 'label-success';
            break;
        case 'trace':
        default:
            val = 'label-default';
            break;
    }
    return val;
});
