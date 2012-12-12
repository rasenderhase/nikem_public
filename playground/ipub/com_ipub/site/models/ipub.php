<?php
/* sicherheitsprüfung: wird die Klasse von Joomla! verwendet? */
defined("_JEXEC") or die();

/* Importiere JModel-Klasse */
jimport("joomla.applicaton.component.model");

/* Die Model-Klasse (von JModel abgeleitet): */
class IpubModelIpub extends JModel
{
	/* Gib auf Anfrage den Text aus: */
	function sagIpub() {
		return "Hallo iPub No.4!";
	}
	
	/* Gib die Liste der Publikationen aus */
	function getPublications() {
		$jAp=& JFactory::getApplication();
		$db =& JFactory::getDBO();
		
		$query = "SELECT guid, autor_name autorName, autor_vorname autorVorname, titel, institut, nummer, year(jahr) jahr FROM #__ipub_publications;";
		$db->setQuery($query);
	
		$result = $db->loadObjectList();
		
		//display and convert to HTML when SQL error
		if (is_null($result)) {
			throw new Exception($db->getErrorMsg(),"error");
		}
			
		return $result;
	}	
}