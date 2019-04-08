
function populate_web_interface(){

    settings = JSON.parse(mwc2config);
    console.log(settings);
    
    html_card_head = `
    <div class="card">
        <div class="card-header" id="heading_{{name}}">
          <a class="mb-0" data-toggle="collapse" data-target="#collapse_{{name}}" aria-expanded="false" aria-controls="collapse_{{name}}">
                <h4 style="margin-bottom:0;">{{fullName}}</h4>
                <small style="font-style: italic; color:#555555;">{{description}}</small>
          </a>
        </div>
        <div id="collapse_{{name}}" class="collapse" aria-labelledby="heading_{{name}}" data-parent="#accordion">
          <div class="card-body">
          
          {{content}}
          
          </div>
        </div>
      </div>`
      
    html_regular_field = `
                <div class="form-group row">
                    <label for="field_{{name}}" class="col-sm-3 col-form-label">{{fullName}}</label>
                    <div class="col-sm-9">
                        <div class="input-group">
                            <input type="text" class="form-control" id="field_{{name}}" aria-describedby="field_{{name}}_desc" placeholder="{{defaultValue}}" value="{{value}}">
                            <div class="input-group-append">
                              <div class="input-group-text">{{unit}}</div>
                            </div>
                        </div>
                        <small id="field_{{name}}_desc" class="form-text text-muted">{{description}}</small>
                    </div>
                  </div>`
    
    blocks_rendered = render_template(settings, html_card_head, html_regular_field);
    document.getElementById("accordion").innerHTML = blocks_rendered;

}


function render_template(data, html_outer_template, html_inner_template){
    var final_html = "";
    
    for (var key in data) {
      block_html = html_outer_template;
      block_html = block_html.split("{{name}}").join(key);
      
      for(var subkey in data[key]){
        block_html = block_html.split("{{"+subkey+"}}").join(data[key][subkey]);
      }
      
      content_html = "";
      console.log(data[key]["value"]);
      // Now process the inner fields
      for(var field in data[key]["value"]){
        console.log(key, field);
      
        if(!field.startsWith("@")){
          field_html = html_inner_template;
          field_html = field_html.split("{{name}}").join(field);
          
          for(var subfield in data[key]["value"][field]){
            field_html = field_html.split("{{"+subfield+"}}").join(data[key]["value"][field][subfield]);
          }
          content_html += field_html;
        }
      }
      block_html = block_html.split("{{content}}").join(content_html);
      
      final_html += block_html;
    }
    
    // Now clean up for all the fields which weren't found and replace with empty string
    final_html = final_html.replace(/\{\{[\s\S]*?\}\}/g, "");
                   
    return final_html;
}
