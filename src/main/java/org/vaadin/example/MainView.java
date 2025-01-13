package org.vaadin.example;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;

@Route
@PermitAll
public class MainView extends VerticalLayout {

    private final H1 counterHeading = new H1();
    private final H2 hostnameHeading = new H2();
    private final H3 ipAddressHeading = new H3();
    private final UnorderedList log = new UnorderedList();
    private final Button button = new Button("Increment");
    private final AtomicInteger counter = new AtomicInteger();

    public MainView() {

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

        counterHeading.addClassName("counter");
        hostnameHeading.addClassName("hostname");
        ipAddressHeading.addClassName("ip-address");

        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_LARGE);
        button.addClickListener(event -> count());

        count();

        add(counterHeading, hostnameHeading, ipAddressHeading, button);
        addAndExpand(log);
    }

    private void count() {
        final var entry = new CountEntry(counter.incrementAndGet(),
                HostInfo.getHostname(), HostInfo.getIpAddress());

        counterHeading.setText(Integer.toString(entry.getCount()));
        hostnameHeading.setText(entry.getHostname());
        ipAddressHeading.setText(entry.getIpAddress());

        final var item = new ListItem("host: " + entry.getHostname() + " ("
                + entry.getIpAddress() + ")" + " | version: "
                + System.getenv("APP_VERSION") + " | session: "
                + VaadinSession.getCurrent().getSession().getId()
                + " | counter: " + entry.getCount());
        log.addComponentAsFirst(item);
    }

    static class CountEntry implements Serializable {

        private final int count;
        private final String hostname;
        private final String ipAddress;

        public CountEntry(int count, String hostname, String ipAddress) {
            this.count = count;
            this.hostname = hostname;
            this.ipAddress = ipAddress;
        }

        public int getCount() {
            return count;
        }

        public String getHostname() {
            return hostname;
        }

        public String getIpAddress() {
            return ipAddress;
        }
    }

    static class HostInfo {
        public static String getHostname() {
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                return localhost.getHostName();
            } catch (Exception e) {
                return "<Unknown>:" + e.getMessage();
            }
        }

        public static String getIpAddress() {
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                return localhost.getHostAddress();
            } catch (Exception e) {
                return "<Unknown>:" + e.getMessage();
            }
        }
    }
}
