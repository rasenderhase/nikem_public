<?php defined('_JEXEC') or die(); 
$document =& JFactory::getDocument();

//jQuery
$document->addScript("http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.js");

//jQuery UI
$document->addScript("http://ajax.googleapis.com/ajax/libs/jqueryui/1/jquery-ui.js");
$document->addScript("http://ajax.googleapis.com/ajax/libs/jqueryui/1/i18n/jquery.ui.datepicker-de.js");
$document->addStyleSheet("http://ajax.googleapis.com/ajax/libs/jqueryui/1/themes/base/jquery-ui.css", "text/css", "all");

//jQuery DataTables from http://datatables.net/
$document->addScript("components/com_ipub/js/datatables/js/jquery.dataTables.js");
$document->addStyleSheet("components/com_ipub/js/datatables/css/demo_table.css", "text/css", "all");

//jQuery validation from http://bassistance.de/jquery-plugins/jquery-plugin-validation/
$document->addScript("http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.js");
$document->addScript("http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/localization/messages_de.js");
$document->addScript("http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/localization/methods_de.js");

//MVC
$document->addScript("components/com_ipub/js/mvc.js");

//iPub
$document->addScript("components/com_ipub/js/app.js");
$document->addStyleSheet("components/com_ipub/css/app.css", "text/css", "all");

?>
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