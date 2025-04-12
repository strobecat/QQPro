package momoi.mod.qqpro.hook

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import com.tencent.qav.controller.c2c.C2COperatorImpl
import momoi.anno.mixin.Mixin

@Mixin
class 通话可使用蓝牙 : C2COperatorImpl() {
    override fun c(i: Int) {
        super.c(i)
        if (isBluetoothHeadsetOn()) {
            e.apply {
                mode = AudioManager.MODE_NORMAL
                startBluetoothSco()
                isBluetoothScoOn = true
                isSpeakerphoneOn = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    clearCommunicationDevice()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun isBluetoothHeadsetOn(): Boolean {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter?.isEnabled != true) return false
        val profiles = listOf(
            BluetoothProfile.A2DP,
            BluetoothProfile.HEADSET,
            BluetoothProfile.HEALTH
        )
        return profiles.any { profile ->
            bluetoothAdapter.getProfileConnectionState(profile) == BluetoothAdapter.STATE_CONNECTED
        }
    }

}