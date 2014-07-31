<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Model Test</title>
<style>
.error {
	color : red;
}
label {
	width : 150px;
	display: inline-block;
}
.numeric {
	text-align: right;
}
li {
	list-style: none;
}
</style>
</head>
<body>

	<form action="model" method="post">
	<input type="hidden" name="changed" value="${it.changed[0]}">
	<input type="hidden" name="changed" value="${it.changed[1]}">
	<ul>
		<li><label>Bezeichnung</label><input type="text" name="NAME" value="${it.format.NAME}"><span class="error">${it.errors.NAME}</span></li>
		<li><label>Einzelpreis</label><input type="text" class="numeric" name="EINZELPREIS" value="${it.format.EINZELPREIS}"><span class="error">${it.errors.EINZELPREIS}</span></li>
		<li><label>Anzahl</label><input type="text" class="numeric" name="STUECKZAHL" value="${it.format.STUECKZAHL}"><span class="error">${it.errors.STUECKZAHL}</span></li>
		<li><label>Gesamt</label><input type="text" class="numeric" name="GESAMTPREIS" value="${it.format.GESAMTPREIS}"><span class="error">${it.errors.GESAMTPREIS}</span></li>
	</ul>
	<input type="submit">
	<input type="reset">
	</form>

</body>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
<script>
$(function() {
	$("input[type='text']").change(function(event) {
		$("[name='changed']").eq(1).val($("[name='changed']").eq(0).val());
		$("[name='changed']").eq(0).val($(event.target).attr("name"));
		$("form").attr("method", "get").submit();
	});
});
</script>
</html>