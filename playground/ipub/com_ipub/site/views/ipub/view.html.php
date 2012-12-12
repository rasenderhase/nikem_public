<?php
/* sicherheitsprüfung: wird die Klasse von Joomla! verwendet? */
defined('_JEXEC') or die();

/* Importiere JView-Klasse: */
jimport('joomla.application.component.view');

class IpubViewIpub extends JView {
	/* Ausgabefunktion */
	function display($tpl = null) {
		/* Hole Model und bitte um gespeicherten Text */
		$model =& $this->getModel();
		$modeldaten = $model->sagIpub();
		/* schiebe den Text ins Template: */
		$this->assignRef('ipub', $modeldaten);

		parent::display($tpl);
	}
}