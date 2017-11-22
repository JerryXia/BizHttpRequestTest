'use strict';

const apiRecords = {
    template: '#apiRecords',
    data: function() {
        return {
            apiRecordsPagedList: [],
            logTbShow: {
                id: false,
                timeStamp: true,
                method: true,
                requestAndQuery: true,
                parameter: true
            },
            apiRecordLogs: [],
            serverstat: {}
        }
    },
    route: {
        data: function(transition){
            //transition.next({
            //    currentPath: transition.to.path
            //})
        }
    },
    created: function(){
        this.fetchData();
    },
    watch: {
        '$route': 'fetchData',
        'serverstat':  function (newVal, oldVal) {
            let html = '<li><a href="http://www.aiswl.com/" target="_blank">lazydev.ReqeustCapture</a></li><li>MemoryStorage</li><li>Server Time: '+ new Date(newVal.time).format('yyyy/MM/dd HH:mm:ss') +'</li><li>Generated: '+newVal.generated+'ns</li>';
            $('.footer ul').html(html).show();
        }
    },
    computed: {
        logCount: function() {
            let that = this;
            return 0;
        }
    },
    methods: {
        fetchData: function() {
            let that = this;

            $.post('apirecords.json', { t: Date.now() }, function(res){
                if(res && res.code == 1) {
                    that.apiRecordsPagedList = res.data.reverse();

                    that.serverstat = res.serverstat;
                } else {
                    that.apiRecordsPagedList = [];
                }
            });
        },
        showLogs: function(recordId, requestId, event) {
            let that = this;
            var $btn = $(event.target).button('loading');

            $.post('apirecordlogs.json?id='+recordId, { t: Date.now() }, function(res) {
                if(res && res.code == 1) {
                    that.apiRecordLogs = res.data;

                    //that.serverstat = res.serverstat;
                } else {
                    that.apiRecordLogs = [];
                }
                $btn.button('reset');
                $('.bs-example-modal-lg').modal();
            });
        },
        replay: function(apiRecord, event){
            let replayUrlHost = '';
            if(window.localStorage){
                replayUrlHost = localStorage.getItem("rquestcapture:replayUrlHost");
            }else{
                alert('This browser does NOT support localStorage');
            }
            if(replayUrlHost == null || replayUrlHost.length === 0){
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
                success: function(res){
                    $btn.button('reset');
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    console.log(jqXHR);console.log(textStatus);console.log(errorThrown);
                    $btn.button('reset');
                }
            });
        },
        parameterFormat: function(obj){
          let formaterObj = {};
          //obj = JSON.parse(JSON.stringify(obj));
          if(obj){
            for(let k in obj){
                let v = obj[k];
                if(Array.isArray(v)){
                  if(v.length === 1){
                    formaterObj[k] = v[0];
                  }else{
                    formaterObj[k] = v;
                  }
                }else{
                  formaterObj[k] = v;
                }
            }
          }
          return formaterObj;
        }
    }
};
const apilogs = {
    template: '#apilogs',
    data: function() {
        return {
            msg: 'Hello, vue router!',
            currentPath: ''
        }
    },
    route: {
        data: function(transition){
            //transition.next({
            //    currentPath: transition.to.path
            //})
        }
    }
};
const allLogs = {
    template: '#allLogs',
    data: function() {
       return {
           logLevels: [ 'ALL', 'DEBUG', 'INFO', 'WARN', 'ERROR', 'FATAL' ],
           queryLevel: '',
           queryFrom: 0,
           queryCount: 10,
           logPagedList: [],
           queryedLogPagedList: [],
           logTbShow: {
               id: false,
               httpRequestRecordId: false,
               timeStamp: true,
               level: true,
               threadName: false,
               loggerName: true,
               message: true,
               host: false,
               ip: false
           },
           serverstat: {}
       }
    },
    created: function(){
        this.fetchData();
    },
    watch: {
        '$route': 'fetchData',
        'serverstat':  function (newVal, oldVal) {
            let html = '<li><a href="http://www.aiswl.com/" target="_blank">lazydev.ReqeustCapture</a></li><li>MemoryStorage</li><li>Server Time: '+ new Date(newVal.time).format('yyyy/MM/dd HH:mm:ss') +'</li><li>Generated: '+newVal.generated+'ns</li>';
            $('.footer ul').html(html).show();
        }
    },
    computed: {
        logCount: function() {
            let that = this;
            return {
                ALL: that.logPagedList.length,
                DEBUG: _.filter(that.logPagedList, function(logItem) { return logItem.level == 'DEBUG'; }).length,
                INFO: _.filter(that.logPagedList, function(logItem) { return logItem.level == 'INFO'; }).length,
                WARN: _.filter(that.logPagedList, function(logItem) { return logItem.level == 'WARN'; }).length,
                ERROR: _.filter(that.logPagedList, function(logItem) { return logItem.level == 'ERROR'; }).length,
                FATAL: _.filter(that.logPagedList, function(logItem) { return logItem.level == 'FATAL'; }).length
            }
        }
    },
    methods: {
        parseLevel: function() {
            let that = this;
            if(that.$route.query.level) {
                if(that.$route.query.level.length > 0) {
                    return that.$route.query.level;
                } else {
                    return 'ALL';
                }
            } else {
                return 'ALL';
            }
        },
        parseFrom: function() {
            let that = this;
            if(that.$route.query.from) {
                return typeof that.$route.query.from === 'string' ? parseInt(that.$route.query.from) : that.$route.query.from;
            } else {
                return 0;
            }
        },
        parseCount: function() {
            let that = this;
            if(that.$route.query.count) {
                return typeof that.$route.query.count === 'string' ? parseInt(that.$route.query.count) : that.$route.query.count;
            } else {
                return 10;
            }
        },
        fetchData: function() {
            let that = this;
            that.queryLevel = that.parseLevel();
            that.queryFrom = that.parseFrom();
            that.queryCount = that.parseCount();
            $.post('alllogs.json', {}, function(res){
                if(res && res.code == 1) {
                    that.logPagedList = res.data.reverse();

                    if(that.queryLevel === 'ALL'){
                        that.queryedLogPagedList = that.logPagedList.slice(that.queryFrom, that.queryFrom + that.queryCount);
                    }else{
                        that.queryedLogPagedList = _.filter(that.logPagedList, function(logItem) {
                            return logItem.level == that.queryLevel;
                        }).slice(that.queryFrom, that.queryFrom + that.queryCount);
                    }

                    that.serverstat = res.serverstat;
                } else {
                    that.logPagedList = [];
                }
            });
        }
    }
};
const settingsReplay = {
    template: '#settings_replay',
    data: function() {
        return {
            replayUrlHost: ''
        }
    },
    created: function(){
        this.loadFromLocalDb();
    },
    methods: {
        loadFromLocalDb: function() {
            let that = this;
            if(window.localStorage){
                that.replayUrlHost = localStorage.getItem("rquestcapture:replayUrlHost");
            }else{
                alert('This browser does NOT support localStorage');
            }
        },
        saveToLocalDb: function() {
            let that = this;
            if(window.localStorage){
                window.localStorage.setItem("rquestcapture:replayUrlHost", that.replayUrlHost);
            }else{
                alert('This browser does NOT support localStorage');
            }
        }
    }
};

const routes = [
    { path: '/apirecords', component: apiRecords },
    { path: '/apilogs', component: apilogs },
    { path: '/alllogs', component: allLogs },
    { path: '/settings_replay', component: settingsReplay },
    { path: '/', redirect: '/apiRecords' }
];
const router = new VueRouter({
    routes: routes
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
            for (var i = 0, zeros = ''; i < (length - value.length) ; i++) {
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
  if(obj){
    for(let k in obj){
        let v = obj[k];
        if(Array.isArray(v)){
          if(v.length === 1){
            formaterObj[k] = v[0];
          }else{
            formaterObj[k] = v;
          }
        }else{
          formaterObj[k] = v;
        }
    }
  }
  return JSON.stringify(formaterObj);
});
Vue.filter('logLevelFormater', function (value) {
  let val = '';
  switch(value.toLowerCase()){
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
