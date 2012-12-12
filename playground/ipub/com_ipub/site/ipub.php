<?php
defined('_JEXEC') or die();

// Controller hinzuholen:
require_once(JPATH_COMPONENT.DS.'controller.php');

// Controller-Objekt erstellen:
$classname = 'IpubController';
$controller = new $classname();

// Die gestellte Aufgabe lösen:
$controller->execute(JRequest::getCmd('task')); //roept default task 'display' aan

// Weiterleiten, sofern der Controller dies verlangt:
$controller->redirect();
?>