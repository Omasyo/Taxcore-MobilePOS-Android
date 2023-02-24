package online.taxcore.pos.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.lc_print_sdk.PrintConfig
import com.example.lc_print_sdk.PrintUtil

class ThermalPrinter {
    companion object {
        fun printReceipt(
            context: Context,
            invoiceNumber: String,
            invoiceText: String = "",
            imageByteArray: ByteArray,
            invoiceFooter: String = "",
        ) {
            PrintUtil.getInstance(context)
            PrintUtil.printText(
                PrintConfig.Align.ALIGN_CENTER,
                PrintConfig.FontSize.TOP_FONT_SIZE_XSMALL,
                false,
                false,
                invoiceText,
            )
            val bitmap = BitmapFactory.decodeByteArray(
                imageByteArray,
                0,
                imageByteArray.size,
                BitmapFactory.Options(),
            )

            val scay: Int = bitmap.width / 300
            val scaledBitmap =
                Bitmap.createScaledBitmap(bitmap, bitmap.width / scay, bitmap.height / scay, true)
            PrintUtil.printBitmap(scaledBitmap.apply {
                println("$width * $height")
            })
            PrintUtil.printText(invoiceFooter)
            PrintUtil.printLine(7)
            PrintUtil.start()

        }
    }
}