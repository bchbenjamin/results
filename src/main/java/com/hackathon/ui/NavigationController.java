package com.hackathon.ui;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller to handle navigation and switching views inside a CardLayout container.
 * Keeps navigation decoupled from specific view panels.
 */
public class NavigationController {
    private final JPanel container;
    private final CardLayout cardLayout;
    private final Map<String, JPanel> registeredViews;

    public NavigationController(JPanel container) {
        if (!(container.getLayout() instanceof CardLayout)) {
            throw new IllegalArgumentException("Container must use CardLayout");
        }
        this.container = container;
        this.cardLayout = (CardLayout) container.getLayout();
        this.registeredViews = new HashMap<>();
    }

    /**
     * Registers a panel view with a unique name.
     */
    public void registerView(String name, JPanel viewPanel) {
        registeredViews.put(name, viewPanel);
        container.add(viewPanel, name);
    }

    /**
     * Retrieves a registered view by name.
     */
    public JPanel getView(String name) {
        return registeredViews.get(name);
    }

    /**
     * Swaps the active display to the specified view.
     */
    public void showView(String name) {
        if (!registeredViews.containsKey(name)) {
            throw new IllegalArgumentException("View " + name + " is not registered");
        }
        cardLayout.show(container, name);
        
        // Notify the panel if it needs updates/refreshes (e.g., reloading table data)
        JPanel view = registeredViews.get(name);
        if (view instanceof ViewLifecycle) {
            ((ViewLifecycle) view).onViewShown();
        }
    }

    /**
     * Functional interface to allow views to hook into lifecycle show/hide events.
     */
    public interface ViewLifecycle {
        void onViewShown();
    }
}
