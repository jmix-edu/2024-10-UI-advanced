package com.company.timesheets.view.main;

import com.company.timesheets.component.slider.Slider;
import com.company.timesheets.entity.TimeEntry;
import com.company.timesheets.event.TimeEntryStatusChangedEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.Route;
import io.jmix.core.DataManager;
import io.jmix.core.LoadContext;
import io.jmix.core.Metadata;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.app.main.StandardMainView;
import io.jmix.flowui.component.main.JmixListMenu;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;

@Route("")
@ViewController("ts_MainView")
@ViewDescriptor("main-view.xml")
public class MainView extends StandardMainView {

    @Autowired
    private Metadata metadata;
    @Autowired
    private DataManager dataManager;
    @ViewComponent
    private JmixListMenu menu;
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onInit(final InitEvent event) {
        updateRejetedTimeEntries();
    }

    @EventListener
    private void TimeEnrtyStatusChanged(TimeEntryStatusChangedEvent event) {
        updateRejetedTimeEntries();
    }

    private void updateRejetedTimeEntries() {

        LoadContext<TimeEntry> loadContext = new LoadContext<>(metadata.getClass(TimeEntry.class));
        loadContext.setQueryString("select e from ts_TimeEntry e " +
                "where e.user.username = :current_user_username " +
                        "and e.status = @enum(com.company.timesheets.entity.TimeEntryStatus.REJECTED)");
        long count = dataManager.getCount(loadContext);

        Span badge = null;

        if (count > 0) {
            badge = new Span("" + count);
            badge.getElement().getThemeList().add("badge error");
        }

        menu.getMenuItem("ts_TimeEntry.my").setSuffixComponent(badge);
    }

    @Subscribe("slider")
    public void onSliderChange(Slider.SlideChangedEvent event) {
        notifications.create("New value is : " + event.getValue())
                .withPosition(Notification.Position.MIDDLE)
                .show();
    }


}
