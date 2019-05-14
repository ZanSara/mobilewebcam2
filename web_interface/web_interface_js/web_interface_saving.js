
function writeConfigFile(){

    values_to_save = $("#entire-form").serializeArray();
    config_dict = JSON.parse(mwc2config);
    
    // First of all, set the managers' fields according to the provided @type
    for(var i=0; i<values_to_save.length; i++){
        var field_value = values_to_save[i].value;
        var field_address = values_to_save[i].name;
        var manager = field_address.split("#")[0];
        var subclass = field_address.split("#")[1].split("-")[0];
        var field_name = field_address.split("-")[1];
    
        if(field_name==="@type" ){
            // Replace the manager fields with the correct ones, taking them from allowedValues
            for(var c=0; c<config_dict[manager].allowedValues.length; c++){
                if(config_dict[manager].allowedValues[c]["@type"] === field_value){
                    config_dict[manager].value = config_dict[manager].allowedValues[c];
                }
            }
        }
    }
    
    // For each field in values_to_save, find where it belongs and replace the value in mwc2config
    for(var v=0; v<values_to_save.length; v++){
        var field_value = values_to_save[v].value;
        var field_address = values_to_save[v].name;
        var manager = field_address.split("#")[0];
        var subclass = field_address.split("#")[1].split("-")[0];
        var field_name = field_address.split("-")[1];
        
        if(subclass !== ""){
            if(subclass === escapeTypeName(config_dict[manager].value["@type"]) ){
                var field_type = config_dict[manager].value[field_name].className;
                config_dict[manager].value[field_name].value = convertIntoProperFormat( field_value, field_type );
            }
        } else {
            var field_type = config_dict[manager].value[field_name].className;
            config_dict[manager].value[field_name].value = convertIntoProperFormat( field_value, field_type );
        }
    }
    
    // Write back the JSON
    mwc2config = JSON.stringify(config_dict, null, 4);
    
    // Load the home page
    load("#", "Control Panel")
}


function convertIntoProperFormat(field_value, field_type){
    if(field_type !== undefined){
        var type = field_type.split(".")[field_type.split(".").length-1];
        console.log("Type: " + type);
        if(type === "Boolean"){
            if(field_value.toLowerCase() === "true") {
                return true;
            } else {
                return false;
            }
        } else if(type === "Integer" || type === "Long"){
            return parseInt(field_value, 10);
            
        } else if(type === "Double"){
            return parseFloat(field_value);
        }
    }
    return field_value;
}
