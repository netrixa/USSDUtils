/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas2nets.ebillsv2.utils;

import com.google.gson.Gson;
import com.vas2nets.ebillsv2.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author VAS2NETS1
 */
public class USSDScreenPaginator {

    public static int ITEMS_PER_PAGE = 4;
    public static String NEXT = "7";
    public static String PREV = "8";

    private List<String> dataSet = new ArrayList<>();
    private int nextIndex;
    private int prevIndex;
    private int currentPage;
    private int selection;
    private String subDataSet = "";
    private int totalPages = 0;
    private Map<String, String> entries = new java.util.HashMap();

    public USSDScreenPaginator(List<String> list, int currentPage, int selection) throws Exception {

        if (list.size() > ITEMS_PER_PAGE) {
            this.setNextIndex(ITEMS_PER_PAGE + 1);
        }

        int pages = (int) Math.ceil(1.0 * list.size() / ITEMS_PER_PAGE);

        this.selection = selection;

        this.setDataSet(list);

        if (this.selection == Integer.valueOf(NEXT)) {
            this.setCurrentPage(currentPage + 1);
        }
        if (this.selection == Integer.valueOf(PREV)) {
            this.setCurrentPage(currentPage - 1);
        }
        this.setTotalPages(pages);

    }

    public void paginate() throws Exception {
        //ITEMS_PER_PAGE+2= Next Index

        boolean isMovingBack = (Integer.valueOf(PREV) == this.selection);
        int fromIndex = 0;
        int toIndex = 0;

        System.out.println("Is Moving Back " + isMovingBack);
        if (!isMovingBack) {
            fromIndex = (getCurrentPage() - 1) * ITEMS_PER_PAGE;
            toIndex = ITEMS_PER_PAGE + (getCurrentPage() - 1) * ITEMS_PER_PAGE;
            getEntries().put(Key.PAGE, "" + (getCurrentPage()));

        } else {
            fromIndex = (getCurrentPage() - 1) * ITEMS_PER_PAGE;
            toIndex = ITEMS_PER_PAGE + (getCurrentPage() - 1) * ITEMS_PER_PAGE;
            getEntries().put(Key.PAGE, "" + (this.getCurrentPage()));

        }

        System.out.println("From: " + fromIndex + "  ToIndex: " + toIndex);
        System.out.println("Is Moving Back " + isMovingBack);

        if (toIndex > this.getDataSet().size()) {
            toIndex = this.getDataSet().size();
        }

        boolean hasMoreItems = true;
        if (toIndex >= this.dataSet.size() && !isMovingBack) {
            toIndex = this.dataSet.size();
            //fromIndex = (getCurrentPage()-2) * ITEMS_PER_PAGE;
            fromIndex = (getCurrentPage() - 1) * ITEMS_PER_PAGE;
            System.out.println(fromIndex);
            int pages = (int) Math.ceil(1.0 * this.dataSet.size() / ITEMS_PER_PAGE);
            this.setTotalPages(pages);
            hasMoreItems = false;
        }
        System.out.println("From: " + fromIndex + "  ToIndex: " + toIndex);
        System.out.println("Current Page: " + this.getCurrentPage());
        System.out.println("Data Set list: " + this.dataSet.size());
        System.out.println("Total Page: " + this.getTotalPages());

        Object[] subItems = null;
        try {
            subItems=this.getDataSet().subList(fromIndex, toIndex).toArray();
        } catch (Exception e) {
            throw new Exception("An Error occured. Please try again later.");
        }

        Gson gson = new Gson();
        String subItemsString = gson.toJson(Arrays.asList(subItems));
        entries.put("sub_items", subItemsString);

        String subDataSet = itemize(subItems);

        int lastIndexOfListedCategory = this.dataSet.size(); //take for instance case 2, it will be 3
        int nextOrMoreItemOption;
        int backOrPreviousItemOption;
        System.out.println("KKKKKKKK" + (this.getCurrentPage() > 1));
        System.out.println("KKKKKKKK2" + hasMoreItems);
        System.out.println("KKKKKKKK3" + (this.getCurrentPage() < this.getTotalPages()));
        if (this.getCurrentPage() > 1 && hasMoreItems && this.getCurrentPage() < this.getTotalPages()) {    //there are still more categories to see: CASE 1
            subDataSet = subDataSet.concat("" + (NEXT)).concat(". Next\n");
            subDataSet = subDataSet.concat("" + (PREV)).concat(". Prev");
        } else if (this.getCurrentPage() == 1 && hasMoreItems) {    //there are still more categories to see:  CASE 3
            subDataSet = subDataSet.concat("" + (NEXT)).concat(". Next\n");
        } else {        //CASE 2
            backOrPreviousItemOption = subItems.length + 1;
            if (this.getTotalPages() > 1) {
                subDataSet = subDataSet.concat(String.valueOf(PREV)).concat(". Prev");
            }
        }


        this.setSubDataSet(subDataSet);
        this.setEntries(entries);

    }

    public String getSubDataSet() {
        return this.subDataSet;
    }

    /**
     * @return the dataSet
     */
    public List<String> getDataSet() {
        return dataSet;
    }

    /**
     * @param dataSet the dataSet to set
     */
    public void setDataSet(List<String> dataSet) {
        Collections.sort(dataSet);
        this.dataSet = dataSet;
    }

    /**
     * @return the nextIndex
     */
    public int getNextIndex() {
        return nextIndex;
    }

    /**
     * @param nextIndex the nextIndex to set
     */
    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }

    /**
     * @return the prevIndex
     */
    public int getPrevIndex() {
        return prevIndex;
    }

    /**
     * @param prevIndex the prevIndex to set
     */
    public void setPrevIndex(int prevIndex) {
        this.prevIndex = prevIndex;
    }

    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return the selection
     */
    public int getSelection() {
        return selection;
    }

    /**
     * @param selection the selection to set
     */
    public void setSelection(int selection) {
        this.selection = selection;
    }

    /**
     * @param subDataSet the subDataSet to set
     */
    public void setSubDataSet(String subDataSet) {
        this.subDataSet = subDataSet;
    }

    public String getSelectedItem() {
        int index = (ITEMS_PER_PAGE * (this.getCurrentPage() - 1)) + this.getSelection();
        return this.dataSet.get(index);
    }

    public static void main(String[] args) {

        double page = (int) Math.ceil(7 * 1.0 / 3);
        System.out.println(page);

    }

    /**
     * @return the entries
     */
    public Map<String, String> getEntries() {
        return entries;
    }

    /**
     * @param entries the entries to set
     */
    public void setEntries(Map<String, String> entries) {
        this.entries = entries;
    }

    /**
     * @return the totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages the totalPages to set
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public static String itemize(Object[] accountList) {

        String accounts = "";
        int i = 0;
        for (Object a : accountList) {

            if (a.toString().contains(":")) {
                //split
                String[] parts = a.toString().split(":");
                accounts += String.format("%d. %s%s", ++i, parts[0].toString(), "\n");
            } else {
                accounts += String.format("%d. %s%s", ++i, a.toString(), "\n");

            }

        }
        return accounts;
    }
}
