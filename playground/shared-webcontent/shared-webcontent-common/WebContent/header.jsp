<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<style>
.header {
	background-color: #9ECFFF;
}

.header img {
	vertical-align: middle;
}

.header h1 {
	font-family: sans-serif;
	font-size: 1.5em;
	display: inline;
	margin: 1em;
	vertical-align: middle;
}

.footer {
	font-family: sans-serif;
	background-color: #9ECFFF;
}
</style>

<div class="header">
    <a href="/shared-webcontent-common"><img src="static/common/logo.jpg" alt="Home"></a><h1>${pageContext.request.contextPath}</h1>
</div>