//MVC framework

//Shimming einiger JavaScript-Funktionen 
//create-Function 
if (typeof Object.create !== "function")
	Object.create = function(o) {
		function F() {
		}
		F.prototype = o;
		return new F();
	};
//console.log
if (typeof console === undefined)
	console = {
		log : function() {
		}
	};
//GUID für die Erzeugung von "eindeutigen" IDs 
Math.guid = function() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	}).toUpperCase();
};

//Publish/Subscribe-"Parent"-Klasse 
var PubSub = {
	setup : function() {
		this.o = jQuery({});
	},

	subscribe : function() {
		this.o.bind.apply(this.o, arguments);
	},

	publish : function() {
		this.o.trigger.apply(this.o, arguments);
	}
};

// Parent-Klasse für Models 
// https://github.com/maccman/book-assets/blob/master/ch03/inherit.html 
var Model = function() {

	var object = function() {
		this.init.apply(this, arguments);
	};
	object.parent = this;

	object.prototype.init = function() {
	};

	/** 
	 * statische Methode: Erzeugt ein einzelnes Model-Objekt der geerbten Model-Klasse 
	 */
	object.init = function() {
		var instance = Object.create(this.prototype);
		instance.parent = this;
		instance.init.apply(instance, arguments);
		return instance;
	};

	object.extend = function(o) {
		jQuery.extend(this, o);
	};

	object.include = function(o) {
		jQuery.extend(this.prototype, o);
	};

	object.find = function(guid) {
		var record = this.records[guid];
		if (!record)
			throw ({
				message : "Unknown record ",
				guid : guid
			});
		return record.dup();
	};

	object.first = function() {
		var first = null;
		$.each(this.records, function(key, value) {
			first = value;
			return false;	//break the loop
		});
		return first;
	};
	
	object.populate = function(values) {
		this.publish("clear", this);
		this.records = {};

		for ( var i = 0, il = values.length; i < il; i++) {
			var record = this.init(values[i]);
			record.newRecord = false;
			this.records[record.guid] = record;
		}
	};

	object.numberOfRecords = function() {
		var count = 0, key = null;
		for (key in this.records) {
			count++;
		}
		return count;
	};

	object.prototype.newRecord = true;
	object.prototype.records = {};
	object.prototype.attributes = [];

	object.prototype.init = function(atts) {
		if (atts)
			this.load(atts);
	};

	object.prototype.load = function(attributes) {
		for ( var name in attributes)
			this[name] = attributes[name];
		this.publish("load", this);
	};

	object.prototype.attributes = function() {
		var result = {};
		for ( var i in this.parent.attributes) {
			var attr = this.parent.attributes[i];
			result[attr] = this[attr];
		}
		result.guid = this.guid;
		return result;
	};

	object.prototype.create = function() {
		if (!this.guid)
			this.guid = Math.guid();
		this.newRecord = false;
		this.parent.records[this.guid] = this.dup();
		this.publish("create", this);
	};

	object.prototype.update = function() {
		this.parent.records[this.guid] = this.dup();
		this.publish("update", this);
	};

	object.prototype.save = function() {
		this.newRecord ? this.create() : this.update();
	};

	object.prototype.destroy = function() {
		delete this.parent.records[this.guid];
		this.publish("destroy", this);
	};

	object.prototype.dup = function() {
		return jQuery.extend(true, {}, this);
	};

	object.prototype.toJSON = function() {
		return (this.attributes());
	};

	object.prototype.publish = function(channel) {
		this.parent.publish(channel, this);
	};

	object.extend(PubSub);
	object.setup();
	return object;
};

// Controller-Klasse 
// https://github.com/maccman/book-assets/blob/master/ch04/finished_controller.html 
(function($, exports) {
	var mod = {};

	mod.create = function(includes) {
		var object = function() {
			this.initializer.apply(this, arguments);
			this.init.apply(this, arguments);
		};

		object.fn = object.prototype;
		object.fn.init = function() {
		};

		object.proxy = function(func) {
			return $.proxy(func, this);
		};
		object.fn.proxy = object.proxy;

		object.include = function(ob) {
			$.extend(this.fn, ob);
		};
		object.extend = function(ob) {
			$.extend(this, ob);
		};

		object.include({
			initializer : function(options) {
				this.options = options;

				for ( var key in this.options)
					this[key] = this.options[key];

				if (this.events)
					this.delegateEvents();
				if (this.elements)
					this.refreshElements();
			},

			$ : function(selector) {
				return $(selector, this.el);
			},

			refreshElements : function() {
				for ( var key in this.elements) {
					this[this.elements[key]] = this.$(key);
				}
			},

			eventSplitter : /^(\w+)\s*(.*)$/,

			delegateEvents : function() {
				for ( var key in this.events) {
					var methodName = this.events[key];
					var method = this.proxy(this[methodName]);

					var match = key.match(this.eventSplitter);
					var eventName = match[1], selector = match[2];

					if (selector === '') {
						this.el.bind(eventName, method);
					} else {
						this.el.delegate(selector, eventName, method);
					}
				}
			}
		});

		if (includes)
			object.include(includes);

		return object;
	};

	exports.Controller = mod;
})($, window);