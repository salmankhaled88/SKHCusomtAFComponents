package skh.adf.custom.components.html;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.ArrayList;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.fragment.RichDeclarativeComponent;

import oracle.jbo.AttributeHints;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;

import skh.adf.custom.shared.ADFUtil;
import skh.adf.custom.shared.JSFUtils;

public class HistoryTracker extends RichDeclarativeComponent {
    
    private String html;
    
    public HistoryTracker() {
    }
    
    @SuppressWarnings("unchecked")
    private String generateBsHTML() {
        StringBuilder str = new StringBuilder();
        DateFormat dateFormate = new SimpleDateFormat("MMM dd, yyyy");
        DateFormat timeFormate = new SimpleDateFormat("hh:mm a");
        String iterName = (String) JSFUtils.resolveExpression("#{attrs.iterName}");

        String customClass = (String) JSFUtils.resolveExpression("#{attrs.customClass}");
        // Representable attributes names
        String circularAttrName = (String) JSFUtils.resolveExpression("#{attrs.circularAttrName}");
        String assignAttrName = (String) JSFUtils.resolveExpression("#{attrs.assignAttrName}");
        String titleAttrName = (String) JSFUtils.resolveExpression("#{attrs.titleAttrName}");
        String subTitleAttrName = (String) JSFUtils.resolveExpression("#{attrs.subTitleAttrName}");
        // CSS Classes
        String circularClass = (String) JSFUtils.resolveExpression("#{attrs.circularClass}");
        String assignClass = (String) JSFUtils.resolveExpression("#{attrs.assignClass}");
        String titleClass = (String) JSFUtils.resolveExpression("#{attrs.titleClass}");
        String subTitleClass = (String) JSFUtils.resolveExpression("#{attrs.subTitleClass}");
        // Flat section properties
        ArrayList<String> flatAttrs = (ArrayList<String>) JSFUtils.resolveExpression("#{attrs.flatDataAttrsNames}");
        String flatLabelClass = (String) JSFUtils.resolveExpression("#{attrs.flatLabelClass}");
        String flatValueClass = (String) JSFUtils.resolveExpression("#{attrs.flatValueClass}");
        // badges section properties
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
        str.append("            <div class=\"historyTracker  " + customClass + "\">");
        str.append("                <div class=\"tracking-list\">");

        // Loop throw Items List
        while (iter.hasNext()) {
            Row row = iter.next();
            if (row == null) {
                continue;
            }
            
            // Item attributes
            Object _circularAttrName = circularAttrName != null ? row.getAttribute(circularAttrName) : "";
            Object _assignDate = row.getAttribute(assignAttrName) != null ? row.getAttribute(assignAttrName) : "";
            Object _titleAttrName = row.getAttribute(titleAttrName);
            Object _subTitleAttrValue = subTitleAttrName != null ? row.getAttribute(subTitleAttrName) : "";
            String date = "-";
            String time = "-";
            if (_assignDate != null) {
                date = dateFormate.format(_assignDate);
                time = timeFormate.format(_assignDate);
            }
            
            // Main item tag
            str.append("<div class=\"tracking-item\">");
            
            // colored circle on line
            // Circular Attribute
            str.append("    <div class=\"tracking-icon " + circularClass + "\">");
            str.append("        <strong>" + _circularAttrName + "</strong>");
            str.append("    </div>");
            
            // Left Contents
            str.append("    <div class=\"tracking-date " + assignClass + "\">")
               .append(         date)
               .append("        <span>" + time + "</span>")
               .append("    </div>");
            
            // Right Contents
            str.append("    <div class=\"tracking-content\">");
            str.append("        <div class=\"mb-2\">");
            str.append("            <span class=\"fw-bold " + titleClass + "\">" + _titleAttrName + "</span>");
            str.append("            <span class=\"text-muted " + subTitleClass + "\"> " + isNull(_subTitleAttrValue+"") + "</span>");
            str.append("        </div>");
            
            // additional lines of data with flat layout
            if (flatAttrs != null && !flatAttrs.isEmpty()) {
            for (String attrName : flatAttrs) {
            
            Object value = row.getAttribute(attrName);
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
            
            Object value = row.getAttribute(attrName) != null;
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
        " * ------------------------------------------------------------------[Custom]---[HistoryTracker]--" + 
        " * Customize view list" + 
        " */" + 
        "" + 
        ".tracking-detail {" + 
        "    padding: 3rem 0" + 
        "}" + 
        "" + 
        "#tracking {" + 
        "    margin-bottom: 1rem" + 
        "}" + 
        "[class*=tracking-status-] p {" + 
        "    margin: 0;" + 
        "    font-size: 1.1rem;" + 
        "    color: #fff;" + 
        "    text-transform: uppercase;" + 
        "    text-align: center" + 
        "}" + 
        "[class*=tracking-status-] {" + 
        "    padding: 1.6rem 0" + 
        "}" + 
        "" + 
        ".tracking-status-intransit {" + 
        "    background-color: #65aee0" + 
        "}" + 
        "" + 
        ".tracking-status-outfordelivery {" + 
        "    background-color: #f5a551" + 
        "}" + 
        "" + 
        ".tracking-status-deliveryoffice {" + 
        "    background-color: #f7dc6f" + 
        "}" + 
        "" + 
        ".tracking-status-delivered {" + 
        "    background-color: #4cbb87" + 
        "}" + 
        "" + 
        ".tracking-status-attemptfail {" + 
        "    background-color: #b789c7" + 
        "}" + 
        "" + 
        ".tracking-status-error, .tracking-status-exception {" + 
        "    background-color: #d26759" + 
        "}" + 
        "" + 
        ".tracking-status-expired {" + 
        "    background-color: #616e7d" + 
        "}" + 
        "" + 
        ".tracking-status-pending {" + 
        "    background-color: #ccc" + 
        "}" + 
        "" + 
        ".tracking-status-inforeceived {" + 
        "    background-color: #214977" + 
        "}" + 
        "" + 
        ".tracking-list {" + 
        "    border: 1px solid #e5e5e5" + 
        "}" + 
        "" + 
        ".tracking-item.concurrent {" + 
        "    background-color: #CECCCC;" + 
        "}" + 
        "" + 
        ".tracking-item {" + 
        "    border-left: 1px solid #e5e5e5;" + 
        "    position: relative;" + 
        "    /*padding: 2rem 1.5rem .5rem 2.5rem;*/" + 
        "    padding: 1rem 2rem;" + 
        "    font-size: 1rem;" + 
        "    margin-left: 3rem;" + 
        "    min-height: 5rem" + 
        "}" + 
        "" + 
        ".tracking-item:last-child {" + 
        "    padding-bottom: 4rem" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-date {" + 
        "    margin-bottom: .5rem" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-date span {" + 
        "    color: #888;" + 
        "    font-size: 85%;" + 
        "    padding-left: .4rem" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-content {" + 
        "    padding: .5rem .8rem;" + 
        "    background-color: #f4f4f4;" + 
        "    border-radius: .5rem" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-content span {" + 
        "    display: block;" + 
        "    color: #888;" + 
        "    font-size: 12px;" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-icon {" + 
        "    line-height: 2.6rem;" + 
        "    position: absolute;" + 
        "    left: -1.3rem;" + 
        "    width: 2.6rem;" + 
        "    height: 2.6rem;" + 
        "    text-align: center;" + 
        "    border-radius: 50%;" + 
        "    font-size: 1.1rem;" + 
        "    background-color: #fff;" + 
        "    color: #000;" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-icon.status-sponsored {" + 
        "    background-color: #f68" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-icon.status-delivered {" + 
        "    background-color: #4cbb87" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-icon.status-outfordelivery {" + 
        "    background-color: #f5a551" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-icon.status-deliveryoffice {" + 
        "    background-color: #f7dc6f" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-icon.status-attemptfail {" + 
        "    background-color: #b789c7;" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-icon.status-inforeceived {" + 
        "    background-color: #214977;" + 
        "}" + 
        "" + 
        ".tracking-item .tracking-icon.status-intransit {" + 
        "    background-color: #e5e5e5;" + 
        "    border: 1px solid #e5e5e5;" + 
        "    font-size: .6rem;" + 
        "}" + 
        ".tracking-item .tracking-icon.status-current {" + 
        "    background-color: Green;" + 
        "    border: 1px solid Green;" + 
        "    font-size: .6rem;" + 
        "    color:#fff;" + 
        "}" + 
        ".tracking-item .tracking-icon.status-reject {" + 
        "    background-color: #d26759;" + 
        "}" + 
        ".status-reject {" + 
        "    border-color: #d26759 !important ;" + 
        "    background-color: rgba(210, 103, 89, 0.25);" + 
        "}" + 
        ".status-approve-con {" + 
        "    border-color: #00e773 !important ;" + 
        "    background-color: rgba(0, 231, 115, 0.25);" + 
        "}" + 
        "" + 
        "@media (min-width:992px) {" + 
        "    .tracking-item {" + 
        "        margin-left: 10rem;" + 
        "    }" + 
        "    .tracking-item .tracking-date {" + 
        "        position: absolute;" + 
        "        left: -10rem;" + 
        "        width: 7.5rem;" + 
        "        text-align: right;" + 
        "    }" + 
        "    .tracking-item .tracking-date span {" + 
        "        display: block;" + 
        "    }" + 
        "    /*.tracking-item .tracking-content {" + 
        "  padding:0;" + 
        "  background-color:transparent" + 
        " }*/" + 
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
