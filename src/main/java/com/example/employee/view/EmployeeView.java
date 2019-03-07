package com.example.employee.view;

import com.example.employee.model.tables.records.EmployeeRecord;
import com.example.employee.service.EmployeeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route
public class EmployeeView extends VerticalLayout {

    private final EmployeeService employeeService;
    private final EmployeeForm employeeForm;

    private Grid<EmployeeRecord> grid = new Grid<>(EmployeeRecord.class);
    private TextField filterText = new TextField();

    public EmployeeView(EmployeeService employeeService, EmployeeForm employeeForm) {
        this.employeeService = employeeService;

        this.employeeForm = employeeForm;

        createUI();
    }

    private void createUI() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList());

        Button clearFilterTextBtn = new Button(VaadinIcon.CLOSE_CIRCLE.create());
        clearFilterTextBtn.addClickListener(e -> filterText.clear());

        HorizontalLayout filtering = new HorizontalLayout(filterText, clearFilterTextBtn);

        Button addEmployeeButton = new Button("Add new employee");
        addEmployeeButton.addClickListener(e -> {
            grid.asSingleSelect().clear();
            employeeForm.setEmployee(new EmployeeRecord());
            employeeForm.setVisible(true);
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addEmployeeButton);

        grid.setColumns("id", "name", "departmentId");
        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(event -> {
            employeeForm.setEmployee(event.getValue());
            employeeForm.setVisible(true);
        });

        // Listen changes made by the form, refresh data from backend
        employeeForm.setChangeHandler(() -> {
            employeeForm.setVisible(false);
            updateList();
        });

        HorizontalLayout main = new HorizontalLayout(grid, employeeForm);
        main.setSizeFull();

        add(toolbar, main);

        setSizeFull();
    }

    public void updateList() {
        grid.setItems(employeeService.findAll(filterText.getValue()));
    }

}