<?php
/* sicherheitsprüfung: wird die Klasse von Joomla! verwendet? */
defined('_JEXEC') or die();

/* Importiere JModel-Klasse */
jimport('joomla.applicaton.component.model');

/* Die Model-Klasse (von JModel abgeleitet): */
class IpubModelHallowelt extends JModel
{
	/* Gib auf Anfrage den Text aus: */
	function sagHallo() {
		return 'Hallo Welt!';
	}
}