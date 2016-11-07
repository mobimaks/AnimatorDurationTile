package uk.co.nickbutcher.animatordurationtile;

import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mobimaks on 09.10.2016.
 */

public final class FinishActivitiesTileService extends TileService {

    private static final String TAG = "FinishActivitiesTileSer";

    @Override
    public void onTileAdded() {
        updateTile();
    }

    @Override
    public void onStartListening() {
        updateTile();
    }

    private void updateTile() {
        final boolean doNotKeepActivitiesEnabled = isDoNotKeepActivitiesEnabled();
        updateTileIcon(doNotKeepActivitiesEnabled);
    }

    private void updateTileIcon(boolean enabled) {
        final Tile tile = getQsTile();
        tile.setIcon(Icon.createWithResource(this, enabled ? R.drawable.ic_dont_on : R.drawable.ic_dont_off));
        tile.updateTile();
    }

    @Override
    public void onClick() {
        super.onClick();
        final boolean newState = !isDoNotKeepActivitiesEnabled();
        if (setDoNotKeepActivities(newState)) {
            updateTileIcon(newState);
        }
    }

    @Override
    public void onTileRemoved() {
        setDoNotKeepActivities(false);
    }

    private boolean setDoNotKeepActivities(boolean enabled) {
        try {
            Settings.Global.putInt(
                    getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, enabled ? 1 : 0);
            return true;
        } catch (SecurityException se) {
            String message = getString(R.string.permission_required_toast);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Log.d(TAG, message);
            return false;
        }
    }

    private boolean isDoNotKeepActivitiesEnabled() {
        try {
            return Settings.Global.getInt(
                    getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES) != 0;
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Could not read Don't keep activities setting", e);
            return false;
        }
    }
}
