package com.example.ffheadshothack;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;
import android.util.Log;

public class HeadshotService extends AccessibilityService {

    private static final String TAG = "FFHeadshot";
    
    // Configuration values (Ideally passed from MainActivity via SharedPrefs)
    private int sensitivity = 50; 
    private boolean isFiring = false;
    
    // Coordinates of the Fire Button (Approximate for most devices)
    // You may need to calibrate these based on screen resolution
    private float fireButtonX = 1080; 
    private float fireButtonY = 1800; 

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Optional: Monitor specific events if needed
    }

    @Override
    public void onInterrupt() {}

    /**
     * Called when the user performs a gesture (tap/drag) on the screen.
     */
    @Override
    public boolean onGesture(int gestureId) {
        // This method is called if we use custom gestures, 
        // but usually we override performGlobalAction or intercept touch directly.
        return super.onGesture(gestureId);
    }

    /**
     * Main Logic: Intercept Touch Events
     */
    @Override
    public void onInterceptTouchEvent(android.view.MotionEvent event) {
        // If the user taps anywhere, we check if it's near the fire button area
        // For simplicity, let's assume we want to auto-headshot EVERY tap 
        // OR we detect if the tap is in the fire zone.
        
        if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
            // Calculate sensitivity offset
            // Higher sensitivity = less vertical lift
            float yOffset = calculateOffset(sensitivity);
            
            Log.d(TAG, "Tap detected at X:" + event.getX() + " Y:" + event.getY());
            
            // Simulate a "Headshot" drag
            // We start at the tap location and drag UP slightly
            performHeadshotDrag(event.getX(), event.getY(), event.getY() - yOffset);
        }
        
        return super.onInterceptTouchEvent(event);
    }

    private void performHeadshotDrag(float startX, float startY, float endY) {
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        
        // Move to start point
        path.moveTo(startX, startY);
        // Drag upwards towards the head
        path.lineTo(startX, endY); 
        
        // Duration: Fast enough to be human-like but precise
        long duration = 150; // milliseconds
        
        GestureDescription.StrokeDescription stroke = 
            new GestureDescription.StrokeDescription(path, 0, duration);
            
        dispatchGesture(builder.build(), null, null);
    }

    private float calculateOffset(int sens) {
        // Inverse relationship: High sens = small offset (easy headshots)
        // Low sens = large offset (needs more precision)
        // Example formula: Base Offset / Sensitivity Factor
        return 100.0f / (sens / 10.0f); 
    }
    
    // Helper to start/stop based on buttons if needed
    public void setSensitivity(int sens) {
        this.sensitivity = sens;
    }
}