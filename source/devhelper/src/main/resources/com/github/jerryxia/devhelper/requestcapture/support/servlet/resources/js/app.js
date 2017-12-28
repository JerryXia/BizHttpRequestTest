'use strict';

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
            apiRecordLogs: [],
            serverstat: {},
            isFetchingData: false,
            fetchedTick: 5,
            fetchedTickInterval: null,
            currFetchHasNew: false,
            lastRecordIndex: 0,
            pageSize: 10
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
        this.fetchData();
    },
    updated: function () {
        //console.log('apiRecords updated');
    },
    destroyed: function () {
        let that = this;
        console.log('apiRecords destroyed');
        if (that.fetchedTickInterval != null) {
            clearInterval(that.fetchedTickInterval);
            that.fetchedTickInterval = null;
        }
    },
    watch: {
        '$route': 'fetchData',
        'serverstat': function (newVal, oldVal) {
            let html = '<li><a href="https://github.com/JerryXia/BizHttpRequestTest" target="_blank">devHelper.ReqeustCapture</a></li><li>MemoryStorage</li><li>Server Time: ' + new Date(newVal.time).format('yyyy/MM/dd HH:mm:ss') + '</li><li>Generated: ' + newVal.generated + 'ns</li>';
            $('.footer ul').html(html).show();
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
                console.log('watch isFetchingData into extra case.');
            }
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
        }
    },
    methods: {
        fetchData: function () {
            let that = this;

            that.isFetchingData = true;
            $.post('allapirecords.json', { t: Date.now() }, function (res) {
                if (res && res.code == 1) {
                    that.lastRecordIndex = res.data.lastIndex;
                    that.apiRecordsPagedList = res.data.list;

                    that.serverstat = res.serverstat;
                    that.currFetchHasNew = res.data.list && res.data.list.length > 0;
                } else {
                    that.apiRecordsPagedList = [];
                    that.currFetchHasNew = false;
                }
                that.isFetchingData = false;
            });
        },
        showLogs: function (recordId, requestId, event) {
            let that = this;
            var $btn = $(event.target).button('loading');

            $.get('apirecordlogs.json', { id: recordId, t: Date.now() }, function (res) {
                if (res && res.code == 1) {
                    that.apiRecordLogs = res.data;

                    //that.serverstat = res.serverstat;
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
                alert('This browser does NOT support localStorage');
            }
            if (replayUrlHost == null || replayUrlHost.length === 0) {
                alert('请先前往settings:replay配置host');
                return;
            }

            let that = this;
            var $btn = $(event.target).button('loading');

            $.ajax({
                method: apiRecord.method,
                url: replayUrlHost + apiRecord.requestURI,// + ((apiRecord.queryString && apiRecord.queryString.length>0) ? '?'+ apiRecord.queryString : ''),
                headers: {},
                data: that.parameterFormat(apiRecord.parameterMap),
                //dataType: 'json',
                success: function (res) {
                    $btn.button('reset');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR); console.log(textStatus); console.log(errorThrown);
                    $btn.button('reset');
                }
            });
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
            $.get('apirecords.json', { startIndex: start, endIndex: end, t: Date.now() }, function (res) {
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

                that.isFetchingData = false;
            });
        }
    }
};
const apilogs = {
    template: '#apilogs',
    data: function () {
        return {
            msg: 'Hello, vue router!',
            currentPath: ''
        }
    },
    route: {
        data: function (transition) {
            //transition.next({
            //    currentPath: transition.to.path
            //})
        }
    }
};
const allLogs = {
    template: '#allLogs',
    data: function () {
        return {
            logLevels: ['ALL', 'DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL'],
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
            let html = '<li><a href="https://github.com/JerryXia/BizHttpRequestTest" target="_blank">devHelper.ReqeustCapture</a></li><li>MemoryStorage</li><li>Server Time: ' + new Date(newVal.time).format('yyyy/MM/dd HH:mm:ss') + '</li><li>Generated: ' + newVal.generated + 'ns</li>';
            $('.footer ul').html(html).show();
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
        }
    },
    methods: {
        fetchData: function () {
            let that = this;

            that.isFetchingData = true;
            $.post('alllogs.json', { t: Date.now() }, function (res) {
                if (res && res.code == 1) {
                    that.lastRecordIndex = res.data.lastIndex;
                    that.logPagedList = res.data.list;

                    that.serverstat = res.serverstat;
                    that.currFetchHasNew = res.data.list && res.data.list.length > 0;
                } else {
                    that.logPagedList = [];
                    that.currFetchHasNew = false;
                }
                that.isFetchingData = false;
            });
        },
        fetchNextData: function () {
            let that = this;

            let start = that.lastRecordIndex;
            let end = that.lastRecordIndex + that.pageSize;

            that.isFetchingData = true;
            $.get('logs.json', { startIndex: start, endIndex: end, t: Date.now() }, function (res) {
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

                that.isFetchingData = false;
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
    },
    methods: {
        loadFromLocalDb: function () {
            let that = this;
            if (window.localStorage) {
                that.replayUrlHost = localStorage.getItem("rquestcapture:replayUrlHost");
            } else {
                alert('This browser does NOT support localStorage');
            }
        },
        saveToLocalDb: function () {
            let that = this;
            if (window.localStorage) {
                window.localStorage.setItem("rquestcapture:replayUrlHost", that.replayUrlHost);
            } else {
                alert('This browser does NOT support localStorage');
            }
        }
    }
};

const router = new VueRouter({
    routes: [
        { path: '/apirecords', component: apiRecords },
        { path: '/apilogs', component: apilogs },
        { path: '/alllogs/:level', component: allLogs },
        { path: '/alllogs', redirect: '/alllogs/ALL' },
        { path: '/settings_replay', component: settingsReplay },
        { path: '/', redirect: '/apiRecords' }
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
Vue.filter('parameterFormater', function (obj, formater) {
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
        case 'info':
            val = 'label-info';
            break;
        case 'warn':
            val = 'label-warning';
            break;
        case 'error':
        case 'fatal':
            val = 'label-danger';
            break;
        case 'debug':
        default:
            val = 'label-default';
            break;
    }
    return val;
});
