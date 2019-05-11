
/**
 * Changes a dict into a list. 
 * The key is retained under the keyword "name" (eventually overwriting such value).
 * Removes all fields starting with "@" (like "@type")
 * "extra_metadata" is added to each element of the list without modifications
 */ 
function dict_to_sorted_list(dict, extra_metadata){
    var list = [];
    for (var key in dict) {
        if(!key.startsWith("@")){
            var entry = dict[key];
            entry["name"] = key;
            for(var extra_val in extra_metadata){
                entry[extra_val] = extra_metadata[extra_val];
            }
            list.push( entry );
        }
    }
    list = list.sort(function(a, b) {return a.position - b.position});
    return list;
}

/**
 * Inject a single value into a single tag pf the specified template
 */
function inject_to_template(template, value, tag){
    return template.split("{{"+tag+"}}").join(value);
}

/**
 * Removes all the tags in the template with an empty string
 */
function cleanup_leftover_tags(template){
    return template.replace(/\{\{[\s\S]*?\}\}/g, "");
}

/**
 * Fills the template using dictioary keys as tags and puts in their corresponding values. 
 * Cleans up unmatching tags.
 */
function populate_template(template, values){
    var populated_template = template;
    for(var key in values){
        populated_template = inject_to_template(populated_template, values[key], key);
    }
    return cleanup_leftover_tags(populated_template);
}

/**
 * Renders a form field according to its metadata
 */ 
function build_form_field(field_template, field_metadata, scope){
    var text_widget_template = document.getElementById("text-widget-template").innerHTML;
                
    widget_html = "";
    // Check which widget is needed
    if(field_metadata.allowedValues !== undefined){
        widget_html = assemble_multichoice_widget( field_metadata.allowedValues, field_metadata.value );
    } else {
        widget_html = text_widget_template;
    }   
    
    // Assemble the entire field
    var form_field = field_template;
    form_field = form_field.split("{{widget}}").join(widget_html);
    
    // Enrich and inject metadata
    field_metadata["scope"] = scope;
    form_field = populate_template(form_field, field_metadata);
    return form_field;                    
}

/**
 * Assembles a multichoice widget. Fetches its own templates from the webpage.
 * Values are supposed to be a list of simple values. 
 * "extra_metadata" instead contains other values that may need to be injected choice-wise. 
 * "extra_metadata" is supposed to be a list with the same order as values.
 * The returning widget may still have leftover tags.
 */
function assemble_multichoice_widget(values, active_choice, extra_metadata){
    var multiple_choice_widget_template = document.getElementById("multiple-choice-widget-template").innerHTML;
    var multiple_choice_entry_template = document.getElementById("multiple-choice-entry-template").innerHTML;

    var choices_list = "";
    for(var v=0; v<values.length; v++){
        var choice_item = multiple_choice_entry_template;
        // select the active button
        if(values[v] === active_choice) {
            choice_item = choice_item.split("{{active}}").join("active");
            choice_item = choice_item.split("{{checked}}").join("checked='checked'");
        }
        // Insert the "choice" value
        choice_item = inject_to_template(choice_item, values[v], "choice");
        // Insert eventual other metadata & cleanup
        if(extra_metadata !== undefined){
            choice_item = populate_template(choice_item, extra_metadata[v] );
        }
        choices_list += choice_item;
    }
    var multichoice_widget = multiple_choice_widget_template;
    multichoice_widget = multichoice_widget.split("{{choices}}").join(choices_list);
    return multichoice_widget;
}

/**
 * Builds the subclasses switch.
 * It has a custom method due to a sensible number of customizations required wrt a normal multichoice field.
 */
function build_type_switch(subclasses_list, page_metadata){
    var form_field_template = document.getElementById("form-field-template").innerHTML;
    var subclass_header_template = document.getElementById("subclass-header-template").innerHTML;
    
    var subclasses_metadata = [];
    var subclasses_names = []
    for(var s=0; s<subclasses_list.length; s++){
        var subclass_metadata = subclasses_list[s];
        subclasses_names.push( subclass_metadata["@type"] );
        subclass_metadata["onfocus"] = ` onfocus="switchLinkedClass( '`+ 
                                                    page_metadata["name"] + `', '` + 
                                                    subclass_metadata["@type"] + `');"`;
        subclass_metadata["scope"] = page_metadata["name"];
        subclass_metadata["name"] = "@type";
        subclasses_metadata.push(subclass_metadata);
    }
    var type_switch_widget = assemble_multichoice_widget( subclasses_names, page_metadata.value["@type"], subclasses_metadata );
    var type_switch_field = form_field_template.split("{{widget}}").join(type_switch_widget);
    
    // Add the subclass header
    type_switch_field += subclass_header_template;
    
    // Modify the metadata and inject it    
    page_metadata["fullname-suffix"] = " Type";
    page_metadata["current-subclass"] = page_metadata.value["@type"];
    type_switch_field = populate_template(type_switch_field, page_metadata);
    
    return type_switch_field;
}

/**
 * Navigation script related to the type switch. 
 * Toggles the subpages
 */
function switchLinkedClass(scope, choice){
    $(".scope-"+scope).hide();
    $(".linkedclass-"+escapeTypeName(choice)+"-"+scope).show();
    $(".subclass-header-"+scope).text("\""+choice+"\" Settings");
}

/**
 * Navigation script related to the type switch. 
 * Creates an escaped version of the type name.
 * NOTE: USED ALSO FOR SAVING, IN THE OTHER JS FILE
 */
function escapeTypeName(wordToEscape){
    if(wordToEscape !== undefined){
        return wordToEscape.replace(/[\W_]+/g,"").toLowerCase();
    }
}



/**
 * Renders the application.
 */
function populate_web_interface(){

    // Get the HTML templates
    var menu_entry_template = document.getElementById("menu-entry-template").innerHTML;
    var menu_subpage_template = document.getElementById("menu-subpage-template").innerHTML; 
    var advanced_settings_box_template = document.getElementById("advanced-settings-box-template").innerHTML;
    var form_field_template = document.getElementById("form-field-template").innerHTML;
    
    // Load the settings
    config_dict = JSON.parse(mwc2config);
    config_list = dict_to_sorted_list(config_dict);
    
    // Create the menu entries and build the main menu
    var main_menu_html = "";
    for (var m=0; m<config_list.length; m++) {
        main_menu_html += populate_template(menu_entry_template , config_list[m]);
    } 
    $("#menu-entries-placeholder").replaceWith( main_menu_html );
      
    
    // Create one page for each menu entry 
    var all_menu_pages_html = "";
    for (var p=0; p<config_list.length; p++) {
        var subclasses_list = config_list[p].allowedValues;
        var menu_subpage = menu_subpage_template;
        
        // List and sort the fields of all the subclasses, if any, and label them properly.
        var fields_list = [];
        if( subclasses_list !== undefined){
            for (var s=0; s<subclasses_list.length; s++){
                var subclass_fields = dict_to_sorted_list(subclasses_list[s], { "subclass": escapeTypeName(subclasses_list[s]["@type"], config_list[p]["name"]) });
                fields_list = fields_list.concat( subclass_fields ); 
            }
        } else {
            fields_list = dict_to_sorted_list(config_list[p].value); 
        }
        
        // Build the page content, including the "Advanced Settings" box
        var regular_fields = "";
        var advanced_fields = ""
        for(var f=0; f<fields_list.length; f++){
            var form_field = build_form_field(form_field_template, fields_list[f], config_list[p]["name"]);
            if(fields_list[f].settingType === "REGULAR"){
                regular_fields += form_field;
            } else {
                advanced_fields += form_field;
            }
        }
        
        // Insert the generated HTML into the page
        menu_subpage = inject_to_template(menu_subpage, regular_fields, "page-content");
        if(advanced_fields !== ""){
            var advanced_box = inject_to_template( advanced_settings_box_template, advanced_fields, "box-content");
            menu_subpage = inject_to_template(menu_subpage, advanced_box, "advanced-box");
        }
        
        // Hide the fields of all the not-relevant subclasses
        if(subclasses_list !== undefined){
            var script = `<script>switchLinkedClass( '`+
                                    config_list[p]["name"] + `', '` + 
                                    config_list[p].value["@type"] +`' );</script> `; 
            menu_subpage = inject_to_template( menu_subpage, script, "custom-scripts");
        }

        // Build the type switch widget, if needed
        if( subclasses_list !== undefined){
            var type_switch = build_type_switch(config_list[p].allowedValues, config_list[p]);
            menu_subpage = inject_to_template(menu_subpage, type_switch, "type-switch");
        }
        
        all_menu_pages_html += populate_template(menu_subpage, config_list[p]);
    }
    
    $("#menu-pages-placeholder").replaceWith( all_menu_pages_html );
    
}
