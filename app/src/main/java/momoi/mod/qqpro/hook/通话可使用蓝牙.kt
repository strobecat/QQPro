package momoi.mod.qqpro.hook

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import com.tencent.qqnt.qav_component_impl.qav.bussiness.QavBussinessCtrl
import momoi.anno.mixin.Mixin
import momoi.mod.qqpro.Utils

@Mixin
class 通话可使用蓝牙 : QavBussinessCtrl() {
    override fun w() {
        Utils.log("eee")
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