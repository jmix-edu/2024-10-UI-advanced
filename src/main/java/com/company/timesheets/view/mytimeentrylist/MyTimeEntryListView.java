package com.company.timesheets.view.mytimeentrylist;


import com.company.timesheets.app.TimeEntrySupport;
import com.company.timesheets.entity.TimeEntry;
import com.company.timesheets.view.timeentry.TimeEntryDetailView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import io.jmix.core.MetadataTools;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "my-time-entries", layout = DefaultMainViewParent.class)
@ViewController("ts_TimeEntry.my")
@ViewDescriptor("my-time-entry-list-view.xml")
public class MyTimeEntryListView extends StandardView {

    @ViewComponent
    private DataGrid<TimeEntry> timeEntriesDataGrid;
    @Autowired
    private TimeEntrySupport timeEntrySupport;
    @Autowired
    private DialogWindows dialogWindows;
    @Autowired
    private MetadataTools metadataTools;

//    @Subscribe("timeEntriesDataGrid.copy")
//    public void onTimeEntriesDataGridCopy(final ActionPerformedEvent event) {
//        TimeEntry selectedItem = timeEntriesDataGrid.getSingleSelectedItem();
//        if (selectedItem == null) {
//            return;
//        }
//
//        TimeEntry copiedEntity = timeEntrySupport.copy(selectedItem);
//
//        DialogWindow<TimeEntryDetailView> dialogWindow = dialogWindows.detail(timeEntriesDataGrid)
//                .withViewClass(TimeEntryDetailView.class)
//                .newEntity(copiedEntity)
//                .build();
//
//        dialogWindow.getView().setOwnTimeEntry(true);
//        dialogWindow.open();
//    }

    @Install(to = "timeEntriesDataGrid.create", subject = "queryParametersProvider")
    private QueryParameters timeEntriesDataGridCreateQueryParametersProvider() {
        return QueryParameters.of(TimeEntryDetailView.PARAMETER_OWN_TIME_ENTRY, "");
    }

    @Install(to = "timeEntriesDataGrid.edit", subject = "queryParametersProvider")
    private QueryParameters timeEntriesDataGridEditQueryParametersProvider() {
        return QueryParameters.of(TimeEntryDetailView.PARAMETER_OWN_TIME_ENTRY, "");
    }


    @Supply(to = "timeEntriesDataGrid.status", subject = "renderer")
    private Renderer<TimeEntry> timeEntriesDataGridStatusRenderer() {
        return new ComponentRenderer<>(Span::new, (span, timeEntry) -> {
            String theme = switch (timeEntry.getStatus()) {
                case NEW -> "";
                case APPROVED -> "success";
                case REJECTED -> "error";
                case CLOSED -> "contrast";
            };

            span.getElement().setAttribute("theme", "badge " + theme);
            span.setText(metadataTools.format(timeEntry.getStatus()));
        });
    }

}