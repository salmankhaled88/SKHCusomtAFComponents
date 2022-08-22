package skh.adf.custom.components.faces;

import com.tangosol.util.stream.RemoteCollectors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.fragment.RichDeclarativeComponent;
import oracle.adf.view.rich.render.ClientEvent;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;

import skh.adf.custom.shared.ADFUtil;
import skh.adf.custom.shared.JSFUtils;


public class SelectManySearchablebox extends RichDeclarativeComponent {

    private List<SelectItem> allItems;
    private List<SelectItem> searchableList;
    private List selectedValues;
    private SelectItem removeSelectedItem;
    private List<SelectItem> actualSelectedValues = new ArrayList<SelectItem>();

    private String keyword;
    private int selectionDisplayRange = 3;

    public SelectManySearchablebox() {
        System.err.println("constructor");
    }
    
    @PostConstruct
    public void init() {
        System.err.println("init > " + getIteratorName());
        String iteratorName = (String) JSFUtils.resolveExpression("#{attrs.iteratorName}");
        String itemValueAttrName = (String) JSFUtils.resolveExpression("#{attrs.itemValueAttrName}");
        String itemLabelAttrName = (String) JSFUtils.resolveExpression("#{attrs.itemLabelAttrName}");

        DCIteratorBinding dc = ADFUtil.findIterator(iteratorName);
        if (dc != null) {
            try {

                RowSetIterator iterator = dc.getRowSetIterator();
                iterator.reset();

                allItems = new ArrayList<SelectItem>();
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    String _itemLabelAttrName = (String) row.getAttribute(itemLabelAttrName);
                    Object _itemValueAttrName = row.getAttribute(itemValueAttrName);
                    allItems.add(new SelectItem(_itemValueAttrName, _itemLabelAttrName));
                }

                System.err.println("initialItems > " + getInitialSelectedItems());
//                List<SelectItem> initialSelectedItems = getInitialSelectedItems();
//                if (initialSelectedItems != null) {
//                    actualSelectedValues.addAll(initialSelectedItems);
//                }

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    ///// Actions //////////////////////////////////////////////////////////////////////////////////////////////////////

    public void filterList(ClientEvent clientEvent) {
        String keyword = (String) clientEvent.getParameters().get("filterKey");
        List<SelectItem> items = getAllItems();
        System.err.println("filter all items > " + items);
        if (items != null) {
            searchableList = items.stream()
                                  .filter(emp -> emp.getLabel()
                                                    .toLowerCase()
                                                    .startsWith(keyword.toLowerCase()))
                                  .collect(RemoteCollectors.toList());
            System.err.println("searchableList > " + searchableList);
        }
    }

    public String removeSelected() {
        if (removeSelectedItem != null) {
            // remove from selected items
            Iterator<SelectItem> empIter = actualSelectedValues.iterator();
            while (empIter.hasNext()) {
                SelectItem emp = empIter.next();
                if (emp.getValue() == removeSelectedItem.getValue()) {
                    empIter.remove();
                    break;
                }
            }
            // handle Show all button visibality
            if (selectionDisplayRange > 3 && actualSelectedValues != null && actualSelectedValues.size() < 5) {
                selectionDisplayRange = 3;
            }
            // add removed item to main arrays
            allItems.add(removeSelectedItem);
            searchableList.add(removeSelectedItem);
        }
        return null;
    }

    /**
     * @param valueChangeEvent
     */
    @SuppressWarnings("unchecked")
    public void selectionListner(ValueChangeEvent valueChangeEvent) {
        List<Integer> values = (List<Integer>) valueChangeEvent.getNewValue();

        System.err.println("selections values > " + values);
        if (values != null) {

            for (Integer item : values) {
                // Remove selected item from searchableList and allItems
                Iterator<SelectItem> iterator = searchableList.iterator();
                while (iterator.hasNext()) {
                    SelectItem element = iterator.next();
                    if (element.getValue() == item) {
                        System.err.println("add selected > " + item);
                        actualSelectedValues.add(element);
                        iterator.remove();
                        // Remove from allItems
                        Iterator<SelectItem> empIter = allItems.iterator();
                        while (empIter.hasNext()) {
                            SelectItem emp = empIter.next();
                            if (emp.getValue() == item) {
                                empIter.remove();
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            setReturnList();
            System.err.println("returnList > " + getReturnList());
        }
    }

    public String showAllSelections() {
        if (selectionDisplayRange == 3) {
            selectionDisplayRange = actualSelectedValues.size();
        } else {
            selectionDisplayRange = 3;
        }
        return null;
    }

    ///// Setters & Getters ////////////////////////////////////////////////////////////////////////////////////////////


    public void setSearchableList(List<SelectItem> actualList) {
        searchableList = actualList;
    }

    public List<SelectItem> getSearchableList() {
        if (searchableList == null) { // || (keyword == null || keyword.equals("") || keyword.equals("null"))) {
            searchableList = allItems;
        }
        return searchableList;
    }

    public List getSelectedValues() {
        if (selectedValues == null) {
            selectedValues = new ArrayList();
        }
        return selectedValues;
    }

    public List<SelectItem> getAllItems() {
        System.err.println("allItems > " + allItems);
        if (allItems == null || allItems.isEmpty()) {
            //            String iteratorName = (String) JSFUtils.resolveExpression("#{attrs.iteratorName}");
            //            String itemValueAttrName = (String) JSFUtils.resolveExpression("#{attrs.itemValueAttrName}");
            //            String itemLabelAttrName = (String) JSFUtils.resolveExpression("#{attrs.itemLabelAttrName}");
            //
            //            allItems = new ArrayList<SelectItem>();
            //
            //            try {
            //                DCIteratorBinding dc = ADFUtil.findIterator(iteratorName);
            //                RowSetIterator iterator = dc.getRowSetIterator();
            //                iterator.reset();
            //                while (iterator.hasNext()) {
            //                    Row row = iterator.next();
            //                    String _itemLabelAttrName = (String) row.getAttribute(itemLabelAttrName);
            //                    Object _itemValueAttrName = row.getAttribute(itemValueAttrName);
            //                    allItems.add(new SelectItem(_itemValueAttrName, _itemLabelAttrName));
            //                }
            //            } catch (NullPointerException e) {
            //                e.printStackTrace();
            //            }

        }
        return allItems;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public List<SelectItem> getActualSelectedValues() {
        return actualSelectedValues;
    }

    public void setRemoveSelectedItem(SelectItem removeSelectedItem) {
        this.removeSelectedItem = removeSelectedItem;
    }

    public SelectItem getRemoveSelectedItem() {
        return removeSelectedItem;
    }

    public int getSelectionDisplayRange() {
        return selectionDisplayRange;
    }

    public void setAllItems(List<SelectItem> allEmployeesList) {
        this.allItems = allEmployeesList;
    }

    public void setSelectedValues(List selectedValues) {
        this.selectedValues = selectedValues;
    }

    public void setActualSelectedValues(List<SelectItem> actualSelectedValues) {
        this.actualSelectedValues = actualSelectedValues;
    }
    
    ///// Declarative Attributes ///////////////////////////////////////////////////////////////////////////////////////

    public List getReturnList() {
        return (List) getAttributes().get("returnList");
    }
    
    public List getInitialSelectedItems() {
        return (List) getAttributes().get("initialSelectedItems");
    }

    public List setReturnList() {
        return (List) getAttributes().put("returnList", actualSelectedValues);
    }

    public String getIteratorName() {
        return (String) getAttributes().get("iteratorName");
    }


}
