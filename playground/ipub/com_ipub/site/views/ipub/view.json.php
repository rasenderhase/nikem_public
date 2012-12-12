<?php
/* sicherheitsprüfung: wird die Klasse von Joomla! verwendet? */
defined('_JEXEC') or die();

/* Importiere JView-Klasse: */
jimport('joomla.application.component.view');

class IpubViewIpub extends JView {
	/* Ausgabefunktion */
	function display($tpl = null) {
		$model = $this->getModel();

		try {
			//$country_id=JRequest::getVar( 'country_id');
			$data = (object) array('message' => $model->sagIpub(), 'records' => $model->getPublications());
			echo json_encode($data);
		} catch (Exception $e) {
    		echo 'Exception abgefangen: ',  $e->getMessage(), "\n";
		}
	}
}