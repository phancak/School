package com.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class GradedItem {

    private SimpleStringProperty gradedItemId;
    private SimpleStringProperty sectionId;
    private SimpleStringProperty gradedItemTitle;
    private SimpleStringProperty gradedItemDescription;
    private SimpleStringProperty gradedItemDate;
    private SimpleStringProperty gradedItemAverage;

    public GradedItem() {
        this.gradedItemId = new SimpleStringProperty(this, "gradedItemId", "n/a");
        this.sectionId = new SimpleStringProperty(this, "sectionId", "n/a");
        this.gradedItemTitle = new SimpleStringProperty(this, "gradedItemTitle", "n/a");
        this.gradedItemDescription = new SimpleStringProperty(this, "gradedItemDescription", "n/a");
        this.gradedItemDate = new SimpleStringProperty(this, "gradedItemDate", "n/a");
        this.gradedItemAverage = new SimpleStringProperty(this, "gradedItemAverage", "n/a");
    }

    public static void processGradedItemTable(ResultSet rs, ObservableList<GradedItem> gradedItemList) {
        GradedItem testGradedItem = null;

        gradedItemList.clear();

        try {
            while (rs.next()) {
                String gradedItemId = rs.getString("Graded_Item_Id");
                String sectionId = rs.getString("Section_Id");
                String gradedItemTitle = rs.getString("Graded_Item_Title");
                String gradedItemDescription = rs.getString("Graded_Item_Description");
                String gradedItemDate = rs.getString("Graded_Item_Date");

                testGradedItem = new GradedItem();
                testGradedItem.setGradedItemId(gradedItemId);
                testGradedItem.setSectionId(sectionId);
                testGradedItem.setGradedItemTitle(gradedItemTitle);
                testGradedItem.setGradedItemDescription(gradedItemDescription);
                testGradedItem.setGradedItemDate(gradedItemDate);

                if (!gradedItemList.contains(testGradedItem)) {
                    gradedItemList.add(testGradedItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GradedItem gradedItem)) return false;

        return Objects.equals(gradedItemId, gradedItem.gradedItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gradedItemId);
    }

    public String getGradedItemId() {
        return gradedItemId.get();
    }

    public StringProperty gradedItemIdProperty() {
        return gradedItemId;
    }

    public void setGradedItemId(String gradedItemId) {
        this.gradedItemId.set(gradedItemId);
    }

    public String getSectionId() {
        return sectionId.get();
    }

    public StringProperty sectionIdProperty() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId.set(sectionId);
    }

    public String getGradedItemTitle() {
        return gradedItemTitle.get();
    }

    public StringProperty gradedItemTitleProperty() {
        return gradedItemTitle;
    }

    public void setGradedItemTitle(String gradedItemTitle) {
        this.gradedItemTitle.set(gradedItemTitle);
    }

    public String getGradedItemDescription() {
        return gradedItemDescription.get();
    }

    public StringProperty gradedItemDescriptionProperty() {
        return gradedItemDescription;
    }

    public void setGradedItemDescription(String gradedItemDescription) {
        this.gradedItemDescription.set(gradedItemDescription);
    }

    public String getGradedItemDate() {
        return gradedItemDate.get();
    }

    public StringProperty gradedItemDateProperty() {
        return gradedItemDate;
    }

    public void setGradedItemDate(String gradedItemDate) {
        this.gradedItemDate.set(gradedItemDate);
    }
}

