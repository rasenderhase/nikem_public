<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page isELIgnored="false"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Willkommen bei iPub</title>

<!-- jQuery -->
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js"></script>

<!-- jQuery UI -->
<script
	src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1/i18n/jquery.ui.datepicker-de.js"></script>
<link rel="stylesheet"
	href="http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/ui-lightness/jquery-ui.css"
	type="text/css" media="all" />

<!-- jQuery DataTables from http://datatables.net/ -->
<script src="js/datatables/js/jquery.dataTables.js"></script>
<link rel="stylesheet" href="js/datatables/css/demo_table.css"
	type="text/css" media="all" />

<!-- jQuery validation from http://bassistance.de/jquery-plugins/jquery-plugin-validation/ -->
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.js"></script>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/localization/messages_de.js"></script>
<script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/localization/methods_de.js"></script>

<!-- MVC -->
<script src="js/mvc.js"></script>

<!-- ipub -->
<script src="js/app.js"></script>
<link rel="stylesheet" href="css/app.css" type="text/css" media="all" />

<link rel="icon" href="images/favicon.gif" type="image/gif">

</head>

<body>
	<h1 style="float: left;">Willkommen bei iPub</h1>
	
	<div id="ipubLoginView" style="float: right;">

	</div>
	
	<div style="clear: both;"></div>
		<div id="ipubFilterView">
		<fieldset>
		<legend>Suche</legend>
		<form class="inputForm">
			<ul>
			<li><label for="ipubQuery">Suche</label><input id="ipubQuery" name="ipubQuery">
			</ul>
			<button id="ipubSearch">Suchen</button>
		</form>
		</fieldset>	
	</div>
	
	<div id="ipubListView">
		<table id="ipubTable" style="width: 100%">
			<thead>
				<tr>
					<th>Autor Name</th>
					<th>Autor Vorname</th>
					<th>Titel</th>
					<th>Institut</th>
					<th>Nummer</th>
					<th>Jahr</th>
					<th></th>
				</tr>
			</thead>
		</table>
		<button id="ipubNew">Neu</button>
	</div>

	<div id="ipubEditView" style="display: none" title="Ipub bearbeiten">
		<form class="inputForm">
			<ul>
				<li><label for="ipubAutorName">Autor Name</label><input id="ipubAutorName" name="ipubAutorName"></li>
				<li><label for="ipubAutorVorname">Autor Vorname</label><input id="ipubAutorVorname" name="ipubAutorVorname"></li>
				<li><label for="ipubTitel">Titel</label><input id="ipubTitel" name="ipubTitel"></li>
				<li><label for="ipubInstitut">Institut</label><input id="ipubInstitut" name="ipubInstitut"></li>
				<li><label for="ipubNummer">Nummer</label><input id="ipubNummer" name="ipubNummer"></li>
				<li><label for="ipubJahr">Jahr</label><input id="ipubJahr" name="ipubJahr" maxlength="4"></li>
			</ul>
		</form>
	</div>
</body>
</html>
