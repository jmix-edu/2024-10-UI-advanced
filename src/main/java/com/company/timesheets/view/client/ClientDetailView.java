package com.company.timesheets.view.client;

import com.company.timesheets.component.ColorComponent;
import com.company.timesheets.component.ColorPicker;
import com.company.timesheets.entity.Client;
import com.company.timesheets.entity.ContactInformation;
import com.company.timesheets.view.contactinformationfragment.ContactInformationFragment;
import com.company.timesheets.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.image.JmixImage;
import io.jmix.flowui.component.textfield.JmixEmailField;
import io.jmix.flowui.component.upload.FileUploadField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstancePropertyContainer;
import io.jmix.flowui.view.*;
import io.jmix.flowui.xml.layout.loader.FragmentLoader;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "clients/:id", layout = MainView.class)
@ViewController("ts_Client.detail")
@ViewDescriptor("client-detail-view.xml")
@EditedEntityContainer("clientDc")
public class ClientDetailView extends StandardDetailView<Client> {


    @ViewComponent
    private JmixImage<byte[]> image;
    @ViewComponent
    private HorizontalLayout uploadWrapper;
    @ViewComponent
    private FileUploadField imageUpload;
    @Autowired
    private Fragments fragments;
    @ViewComponent
    private InstancePropertyContainer<ContactInformation> contactInformationDc;
    @Autowired
    private Notifications notifications;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        updateImage(getEditedEntity().getImage());
        applySecurityPermissions();
    }

    private void applySecurityPermissions() {
        uploadWrapper.setVisible(!imageUpload.isReadOnly());
    }

    private void updateImage(byte[] value) {
        if (value == null) {
            image.setSrc("images/add-image-placeholder.png");
        }
    }

    @Subscribe(id = "uploadClearBtn", subject = "clickListener")
    public void onUploadClearBtnClick(final ClickEvent<JmixButton> event) {
        getEditedEntity().setImage(null);
    }

    @Subscribe(id = "clientDc", target = Target.DATA_CONTAINER)
    public void onClientDcItemPropertyChange(final InstanceContainer.ItemPropertyChangeEvent<Client> event) {
        if ("image".equals(event.getProperty())) {
            updateImage(getEditedEntity().getImage());
        }
    }

    @Subscribe
    public void onInit(final InitEvent event) {
//        ColorPicker colorPicker = new ColorPicker();
//        getContent().add(colorPicker);
//
//        colorPicker.addValueChangeListener(event1 -> {
//            notifications.show("Color: " + event1.getValue());
//        });

        ColorComponent component = new ColorComponent();
        getContent().add(component);
    }

//    @Subscribe
//    public void onInit(final InitEvent event) {
//        ContactInformationFragment fragment = fragments.create(this, ContactInformationFragment.class);
//        fragment.setContactInformationValues(contactInformationDc);
//        getContent().add(fragment);
//    }
    
    
    
}