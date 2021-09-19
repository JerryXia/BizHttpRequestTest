# BizHttpRequestTest

This is a helper tool library in developing or testing java web application.

If you are debugging apis with front-end engineers, you may encounter the following situation, they tell you that one of the apis returns an error, then you will ask them to send you their request details, though you have logged the request records in your application. Then you will copy to Postman to testing the reqeust on your localhost, and debugging step by step, this library can help you resolve this problem.


## Quick Start

By default, this library will working with zero configuration. In servlet-api supported web containers, you can add the following maven dependency.

### Maven

    <dependency>
      <groupId>com.github.jerryxia</groupId>
      <artifactId>devhelper</artifactId>
      <version>1.1.2</version>
    </dependency>

if you use spring boot, Add the following dependency.

    <dependency>
      <groupId>com.github.jerryxia</groupId>
      <artifactId>devhelper-spring-boot</artifactId>
      <version>1.1.2</version>
    </dependency>

	#devhelper.request-id-init.request-id-response-header-name=X-Call-RequestId
	devhelper.request-capture.enabled=true
	#devhelper.request-capture.exclusions=*.js,*.gif,*.jpg,*.png,*.css,*.ico,/robots.txt,/admin/requestcapture/*,/admin/elmah/*,/admin/druid/*,/admin/management/*
	#devhelper.request-capture.replay-request-id-request-header-name=
	#devhelper.request-capture.max-payload-length=10240
	devhelper.request-capture-servlet.enabled=true
	#devhelper.request-capture-servlet.url-pattern=/admin/requestcapture/*
	#devhelper.request-response-log.enabled=true
	#devhelper.request-response-log.log-request-header-names=Authorization,Cookie,Referer
	#devhelper.elmah-servlet.enabled=true
	#devhelper.elmah-servlet.url-pattern=/admin/elmah/*
	#devhelper.elmah-servlet.error-record-storage=
	#devhelper.elmah-servlet.error-record-file-storage-path=
	#devhelper.task-run.lazy-mode=true


## Dashboard

[demo](https://res4gqk.appspot.com/)

![sample01](doc/sample01.gif)




