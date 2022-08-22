package skh.adf.custom.components.html;

import java.util.ArrayList;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.fragment.RichDeclarativeComponent;

import oracle.jbo.AttributeHints;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;

import skh.adf.custom.shared.ADFUtil;
import skh.adf.custom.shared.JSFUtils;

public class VerticalTracker extends RichDeclarativeComponent {
    
    private String html;

    public VerticalTracker() {
    }
    
    @SuppressWarnings("unchecked")
    private String generateBsHTML() {
        StringBuilder str = new StringBuilder();
        String iterName = (String) JSFUtils.resolveExpression("#{attrs.iterName}");

        // Representable attributes names
        String customClass = (String) JSFUtils.resolveExpression("#{attrs.customClass}");
        String titleAttrName = (String) JSFUtils.resolveExpression("#{attrs.titleAttrName}");
        String subTitleAttrName = (String) JSFUtils.resolveExpression("#{attrs.subTitleAttrName}");
        String circularAttrName = (String) JSFUtils.resolveExpression("#{attrs.circularAttrName}");
        String circularClass = (String) JSFUtils.resolveExpression("#{attrs.circularClass}");
        String titleClass = (String) JSFUtils.resolveExpression("#{attrs.titleClass}");
        String subTitleClass = (String) JSFUtils.resolveExpression("#{attrs.subTitleClass}");
        ArrayList<String> flatAttrs = (ArrayList<String>) JSFUtils.resolveExpression("#{attrs.flatDataAttrsNames}");
        String flatLabelClass = (String) JSFUtils.resolveExpression("#{attrs.flatLabelClass}");
        String flatValueClass = (String) JSFUtils.resolveExpression("#{attrs.flatValueClass}");
        ArrayList<String> badgeAttrsNames = (ArrayList<String>) JSFUtils.resolveExpression("#{attrs.badgeAttrsNames}");
        String badgeClass = (String) JSFUtils.resolveExpression("#{attrs.badgeClass}");

        // Iterate
        DCIteratorBinding dc = ADFUtil.findIterator(iterName);
        ViewObject object = dc.getViewObject();
        RowSetIterator iter = dc.getViewObject().createRowSetIterator("");
        
        // Build HTML body
        str.append("<div class=\"container-fluid\">");
        str.append("    <div class=\"row\">");
        str.append("        <div class=\"col-md-12 col-lg-12 px-0\">");
        str.append("            <div class=\"verticalTracker " + customClass + "\">");
        str.append("                <div class=\"tracking-list\">");

        // Loop throw Items List
        while (iter.hasNext()) {
            Row row = iter.next();
            if (row == null) {
                continue;
            }
            
            // Item attributes
            Object _circularAttrValue = circularAttrName != null ? row.getAttribute(circularAttrName) : "";
            Object _titleAttrValue = row.getAttribute(titleAttrName);
            Object _subTitleAttrValue = row.getAttribute(subTitleAttrName) != null ? row.getAttribute(subTitleAttrName) : "";
            
            // Main item tag
            str.append("<div class=\"tracking-item\" >");
            
            // colored circle on line
            // Circular Attribute
            str.append("    <div class=\"tracking-icon " + circularClass + "\">");
            str.append("        <strong>" + _circularAttrValue + "</strong>");
            str.append("    </div>");

            // Right Contents
            str.append("    <div class=\"tracking-content\">");
            str.append("        <div class=\"mb-2\">");
            str.append("            <span class=\"fw-bold " + titleClass + "\">" + _titleAttrValue + "</span>");
            str.append("            <span class=\"text-muted " + subTitleClass + "\"> " + isNull(_subTitleAttrValue+"") + "</span>");
            str.append("        </div>");
            
            // additional lines of data with flat layout
            if (flatAttrs != null && !flatAttrs.isEmpty()) {
            for (String attrName : flatAttrs) {
            
            Object value = row.getAttribute(attrName) != null ? row.getAttribute(attrName) : "-";
            String label = (String) (object.findAttributeDef(attrName)).getProperty(AttributeHints.ATTRIBUTE_LABEL);
            
            str.append("        <div>");
            str.append("            <div class=\"d-flex align-items-center\">");
            str.append("                <span class=\"text-muted me-3 item-label " + flatLabelClass + "\">" + label + "</span>");
            str.append("                <span class=\"" + flatValueClass + "\"> " + isNull(value+"") + "</span>");
            str.append("            </div>");
            str.append("        </div>");
            
            }
            }
            // [CLOSE] additional lines of data with flat layout
            
            // badges sections
            if (badgeAttrsNames != null && !badgeAttrsNames.isEmpty()) {
            for (String attrName : badgeAttrsNames) {
            
            Object value = row.getAttribute(attrName) != null ? row.getAttribute(attrName) : "-";
            str.append("        <div class=\"badge " + badgeClass + "\">");
            str.append(             isNull(value+""));
            str.append("        </div>");
                
            }
            }
            // [close] badges sections
            
            // [close] tracking-content
            str.append("    </div>");
            
            // Append custom css
//            str.append("<style>");
//            str.append(getTrackerCss());
//            str.append("</style>");
            
            // [close] tracking-icon
            str.append("</div>");
        }
        //////////////////////////////////////////////////////////////////////////////
        // Close HTML tags
        str.append("                </div>");
        str.append("            </div>");
        str.append("        </div>");
        str.append("    </div>");
        str.append("</div>");

        return str.toString();
    }
    
    private String getTrackerCss() {
        return "" + 
        "/*" + 
        " *" + 
        " * ------------------------------------------------------------------[Custom]---[verticalTracker]--" + 
        " * Customize view list" + 
        " */" + 
        "" + 
        ".verticalTracker {" + 
        "    margin-bottom: 1rem" + 
        "}" + 
        ".verticalTracker .tracking-item {" + 
        "    border-left: 1px dashed #e5e5e5;" + 
        "    position: relative;" + 
        "    /*padding: 2rem 1.5rem .5rem 2.5rem;*/" + 
        "    padding: 1rem 2rem;" + 
        "    /*margin-left: 3rem;*/" + 
        "    margin-left: 2rem;" + 
        "    min-height: 5rem" + 
        "}" + 
        ".verticalTracker .tracking-item .tracking-date {" + 
        "    margin-bottom: .5rem" + 
        "}" + 
        ".verticalTracker .tracking-item .tracking-content .item-label {" + 
        "    font-size: var(--font-size-md);" + 
        "}" + 
        ".verticalTracker .tracking-item .tracking-content {" + 
        "    /*padding: 0px 0.5rem .8rem;*/" + 
        "    padding: 0px 1rem .8rem;" + 
        "}" + 
        ".verticalTracker .tracking-item .tracking-content span {" + 
        "    display: block;" + 
        "}" + 
        ".verticalTracker .tracking-item .tracking-icon {" + 
        "    /*line-height: 2.6rem;*/" + 
        "    line-height: 3.6rem;" + 
        "    position: absolute;" + 
        "    /*left: -1.3rem;" + 
        "    width: 2.6rem;*/" + 
        "    /*left: -1.5rem;*/" + 
        "    left: -2.1rem;" + 
        "    /*width: 3rem;*/" + 
        "    width: 4rem;" + 
        "    /*height: 2.6rem;*/" + 
        "    height: 3.5rem;" + 
        "    text-align: center;" + 
        "    border-radius: 20%;" + 
        "    font-size: var(--font-size-xl);" + 
        "    /*background-color: var(--primary-color);*/" + 
        "    background-color: rgb(108,117,125,.75);" + 
        "    color: #fff !important;" + 
        "}" +
        ":root {" + 
        "    /* Fonts */" + 
        "    --application-font-size: 1rem;" + 
        "    --font-size-sm: 60%;" + 
        "    --font-size-md: 80%;" + 
        "    --font-size-lg: 120%;" + 
        "    --font-size-xl: 140%;" + 
        "    --font-size-2x: 160%;" + 
        "    --font-size-3x: 180%;" + 
        "}";
    }
    
    public String getHtml() {
        html = generateBsHTML();
        return html;
    }    
    
    private String isNull(String value) {
        return value == null || value.equalsIgnoreCase("null") || value.equals("") ? "-" : value;
    }
    
}
