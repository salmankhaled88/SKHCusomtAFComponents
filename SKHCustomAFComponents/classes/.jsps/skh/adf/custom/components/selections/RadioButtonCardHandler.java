// Do not edit this file!
// This file has been automatically generated by ADF.
// 
package skh.adf.custom.components.selections;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.ComponentConfig;
import oracle.adfinternal.view.faces.facelets.rich.IncludeHandler;
import oracle.adf.view.rich.component.rich.fragment.RichDeclarativeComponent;

public class RadioButtonCardHandler extends IncludeHandler {

  private static final String _VIEW_ID = "/components/faces/radiobuttons/RadioButtonCard.jspx";

  public RadioButtonCardHandler(ComponentConfig config) {
    super(config);
  }

  protected String getViewId(FaceletContext ctx) {
    return _VIEW_ID;
  }

  protected RichDeclarativeComponent createComponent() {
    return new RadioButtonCardComponent();
  }

}