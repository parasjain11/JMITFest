package com.jmit.festmanagement.data;

/**
 * Created by lively on 12/9/16.
 */
public class DrawerItem
{
    private String title;
    private String[] child;
    boolean expanded=false;
    public DrawerItem(String title, String[] child)
    {
        this.child = child;
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public String[] getChild()
    {
        return child;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
