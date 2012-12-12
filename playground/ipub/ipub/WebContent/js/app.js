//Generated :-)

//Field: autorName, Text Field, "Autor Name"
//Field: autorVorname, Text Field, "Autor Vorname"
//Field: titel, Text Field, "Titel"
//Field: institut, Text Field, "Institut"
//Field: nummer, Number Field, "Nummer"
//Field: jahr, Date Field, "Jahr"

$(function() {
	var IpubEditView, ipubEditView, IpubListView, IpubFilterView = null, ipubFilterView = null,
	Ipub = new Model,
	IpubEdit = new Model;
	
	Ipub.extend({
		getRemote : function(href, aoData, fnCallback) { 
		
			//read search filter data
			aoData.push( { name : "query", value : ipubFilterView.query.val() } );
			
			$.ajax({ 
				url : href, 
				dataType : "json", 
				type : "GET",
				data : aoData,
				success : function (data) { 
					if (data.records) { 
						Ipub.populate(data.records); 
					} 
					Ipub.publish("getRemote", Ipub.records);
					fnCallback(data);
				} 
			}); 
		}
	});
	
	IpubEdit.extend({
		getRemote : function(href) { 
			$.ajax({ 
				url : href,
				dataType : "json", 
				type : "GET", 
				success : function (data) { 
					if (data.records) { 
						IpubEdit.populate(data.records); 
					} 
					IpubEdit.publish("getRemote", IpubEdit.records); 
				} 
			}); 
		}
	});
	
	IpubEdit.include({
		saveRemote : function() {
			var data;
			this.save();
			data = this.attributes(); 
			
			$.ajax({ 
				url : this.href, 
				data : data, 
				dataType : "json", 
				type : "POST", 
				success : $.proxy(function(data, textStatus, jqXHR) { 
					if (data && data.records) {
						var recordData = data.records[0]; 
						$.extend(this, recordData);
						this.save(); 
						this.publish("saveRemote", this); 
					}
				}, this)
			}); 
		}
	});
	
	Ipub.attributes = [ "guid", "href", "autorName", "autorVorname", "titel", "institut", "nummer", "jahr" ];
	Ipub.populate({});
	
	IpubEdit.attributes = [ "guid", "href", "autorName", "autorVorname", "titel", "institut", "nummer", "jahr" ];
	IpubEdit.populate({});

	IpubFilterView = Controller.create({
		elements : { 
			"form" : "form",
			"#ipubQuery" : "query",
			"#ipubSearch" : "search"
		},
		
		events : { 
			"submit form" : "searchRecords"
		},
		
		init : function() {
			$.extend(this, PubSub);
			this.setup();
			this.search.button();
		},
		
		searchRecords : function(event) {
			event.preventDefault();
			this.publish("searchRecords");
			return false;
		}
	});
	
	ipubFilterView = new IpubFilterView({ el : $("#ipubFilterView")});
	
	// IpubEditView ****************** 
	IpubEditView = Controller.create({ 
		elements : { 
			"form" : "form",
			"#ipubAutorName" : "autorName",
			"#ipubAutorVorname" : "autorVorname",
			"#ipubTitel" : "titel",
			"#ipubInstitut" : "institut",
			"#ipubNummer" : "nummer",
			"#ipubJahr" : "jahr"
		}, 
		
		events : { 
			"submit form" : "saveRecord"
		},
		
		init : function() { 
			this.el.dialog({ 
				autoOpen : false, 
				modal : true, 
				width : 900, 
				resizable : false, 
				buttons : [ 
					   { 
						   text : "Speichern", 
						   click : function(){ 
							   $(this).find("form").trigger("submit"); 
						   }
					   }, 
					   { 
						   text : "Abbrechen", 
						   click : $.proxy(this.cancel, this),
					   } 
				] 
			}); 
			
			this.form.validate({
				rules : { 
					ipubAutorName : { 
						required : true
					},
					ipubAutorVorname : { 
						required : true
					},
					ipubTitel : { 
						required : true
					},
					ipubInstitut : { 
						required : true
					},
					ipubNummer : { 
					},
					ipubJahr : { 
						min: 1000,
						max : 9999,
						required: true,
						number: true
					}
				},
				messages : { 
					ipubAutorName : { 
						required : "Mussfeld nicht gefüllt"
					},
					ipubAutorVorname : { 
						required : "Mussfeld nicht gefüllt"
					},
					ipubTitel : { 
						required : "Mussfeld nicht gefüllt"
					},
					ipubInstitut : { 
						required : "Mussfeld nicht gefüllt"
					},
					ipubNummer : { 
					},
					ipubJahr : { 
						min : "Bitte vierstellig",
						max : "Bitte vierstellig",
						number : "Ungültiges Jahr",
						required: "Mussfeld nicht gefüllt"
					}
				}
			});
			
			IpubEdit.subscribe("create", $.proxy(this.onCreate, this));
			IpubEdit.subscribe("update", $.proxy(this.onUpdate, this));
			IpubEdit.subscribe("load", $.proxy(this.onLoad, this));
			IpubEdit.subscribe("saveRemote", $.proxy(this.onSaveRemote, this));
		},
		
		// Handlers for View Events --> Update Model
		openDialog : function(href) {
			var record;
			if (href) {
				IpubEdit.getRemote(href);				
			} else {
				IpubEdit.populate({});
				record = IpubEdit.init({ 
					href : "publication",
					autorName : null,
					autorVorname : null,
					titel : null,
					institut : null,
					nummer : null,
					jahr : null
				});
				record.save();
			}
			this.el.dialog("open");
		},
		saveRecord : function(event) {
			var record;
			if (this.form.valid()) {
				event.preventDefault();
				record = IpubEdit.first();
				record.autorName = this.autorName.val();
				record.autorVorname = this.autorVorname.val();
				record.titel = this.titel.val();
				record.institut = this.institut.val();
				record.nummer = this.nummer.val();
				record.jahr = this.jahr.val();
				record.saveRemote();
			}
			return false;
		},
		cancel : function() {
			this.form.validate().reset();
			this.form.validate().hideErrors();
			this.form.find("label.error").remove();
			this.form.find(".error").removeClass("error");
			this.el.dialog("close");
		}, 
		
		//Handlers for Model Events -> Update View
		onCreate : function(event, record) {
			console.log("created");
			console.log(record);
		},
		onUpdate : function(event, record) {
			console.log("updated");
			console.log(record);
		},
		onLoad : function(event, record) {
			console.log("loaded");
			console.log(record);
			
			this.autorName.val(record.autorName);
			
			this.autorVorname.val(record.autorVorname);
			
			this.titel.val(record.titel);
			
			this.institut.val(record.institut);
			
			this.nummer.val(record.nummer);
			
			this.jahr.val(record.jahr);
			
		},
		onSaveRemote : function(event, revord) {
			this.el.dialog("close");
		}
	});
	ipubEditView = new IpubEditView({ el : $("#ipubEditView") });
	
	IpubListView = Controller.create({
		elements : { 
			"#ipubTable" : "table",
			"#ipubNew" : "newButton"
		},
		
		events : {
			"click #ipubNew" : "openNewDialog",
			"mouseenter .icon-button" : "iconButtonMouseenter",
			"mouseleave .icon-button" : "iconButtonMouseleave",
			"click table td" : "clickRecord"
		},
		
		iconButtonMouseenter : function(event) {
			 $(event.currentTarget).addClass('ui-state-hover');
		},
		
		iconButtonMouseleave : function(event) {
			 $(event.currentTarget).removeClass('ui-state-hover');
		},
		
		init : function() {
			this.newButton.button();
			
			this.table.dataTable({
				bJQueryUI : true,               // use jquery theme. 
				bPaginate : true,              	// Pagination 
				bLengthChange : true,			// Select number of displayed records
				bFilter : false,                	// display filter of displayed records
				bInfo : true,                  	// display info about number of displayed records
				bAutoWidth : true,          	// automatic column width detection
				bServerSide : true,
				sAjaxSource : "publication",
				sAjaxDataProp : "records",
				fnServerData : function ( sSource, aoData, fnCallback ) {
					Ipub.getRemote(sSource, aoData, fnCallback);
				},
				fnRowCallback : function (nRow, aData, iDisplayIndex) {
					nRow.id = "ipubListTable/" + aData.guid;
					return nRow;
				},
				aoColumns : [
				             {
								mDataProp : "autorName"
							}
				             ,
				             {
								mDataProp : "autorVorname"
							}
				             ,
				             {
								mDataProp : "titel"
							}
				             ,
				             {
								mDataProp : "institut"
							}
				             ,
				             {
								mDataProp : "nummer"
							}
				             ,
				             {
								mDataProp : "jahr"
							}
				             ,
				             {
				            	 mDataProp: function ( source, type, val ) {
				            		return "<div style='float:left' class='ui-state-default ui-corner-all icon-button' title='delete'><span class='ui-icon ui-icon-trash'></span></div>"
				            			+ "<div style='clear:both'></div>";
				            	 },
				                 bSortable : false
				             }
				             ]
			});

			ipubFilterView.subscribe("searchRecords", $.proxy(this.searchRecords, this));
			IpubEdit.subscribe("saveRemote", $.proxy(this.onSaveRemote, this));

			Ipub.subscribe("create", $.proxy(this.onCreate, this));
			Ipub.subscribe("update", $.proxy(this.onUpdate, this));
			Ipub.subscribe("destroy", $.proxy(this.onDestroy, this));
		},
		
		// Handlers for View Events --> Update Model
		openNewDialog : function(event) {
			ipubEditView.openDialog();
		},
		clickRecord : function(event) {
			var clickedTd = event.currentTarget; 
             /* Get the position of the current data from the TD node */ 
			 var aPos = this.table.fnGetPosition(clickedTd); 
			 /* Get the data for this row */ 
			 var record = this.table.fnGetData(aPos[0]); 
			 var colIndex = aPos[2];
			 
			 if (colIndex == 6) {
				 //delete clicked‚
				 record.destroy();
			 } else {
				 ipubEditView.openDialog(record.href);
			 }
		},
		searchRecords : function(event) {
			this.table.fnDraw();
		},
		
		//Handlers for Model Events -> Update View
		onClear : function(event) {
			this.table.fnClearTable();
		},

		onDestroy : function(event, record) {
			var $tr = this.table.find("tr[id='ipubListTable/" + record.guid + "']");
			this.table.fnDeleteRow($tr[0], true);
		},
		
		onUpdate : function(event, record) {
			this.table.fnDraw();
		},
		
		onCreate : function(event, record) {
			this.table.fnDraw();
		},
		
		onLoad : function(event, record) {
			if (record.guid) {
				this.table.fnAddData(record, true);
			}
		},
		
		onSaveRemote : function(event, record) {
			this.table.fnDraw();
		}
	});
		
	new IpubListView({el : $("#ipubListView")});
});