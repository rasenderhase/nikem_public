<?php
/* sicherheitsprüfung: wird die Klasse von Joomla! verwendet? */
defined('_JEXEC') or die();

/* Importiere JView-Klasse: */
jimport('joomla.application.component.view');

/**
 *  Die View-Klasse (von JView abgeleitet)
 */
class IpubViewHallowelt extends JView {
	/* Ausgabefunktion */
	function display($tpl = null) {
		/* Hole Model und bitte um gespeicherten Text */
		$model =& $this->getModel();
		$modeldaten = $model->sagHallo();
		/* schiebe den Text ins Template: */
		$this->assignRef('gruesse', $modeldaten);
		
		parent::display($tpl);
	}
}