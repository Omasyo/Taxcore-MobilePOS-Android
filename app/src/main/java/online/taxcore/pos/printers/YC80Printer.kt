package online.taxcore.pos.printers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.lc_print_sdk.PrintConfig
import com.example.lc_print_sdk.PrintConfig.IErrorCode
import com.example.lc_print_sdk.PrintUtil

object YC80Printer : Printer {

    private var state = PrinterState.SUCCESS

    init {
        PrintUtil.setPrintEventListener(listener)
    }

    object listener : PrintUtil.PrinterBinderListener {
        override fun onPrintCallback(errorCode: Int) {
            state = when (errorCode) {
                IErrorCode.ERROR_NO_ERROR -> PrinterState.SUCCESS
                IErrorCode.ERROR_DEV_IS_BUSY -> PrinterState.BUSY
                IErrorCode.ERROR_PRINT_NOPAPER -> PrinterState.OUTOFPAPER
                else -> PrinterState.ERROR
            }
        }

        override fun onVersion(p0: String?) {}

    }

    override fun print(
        invoiceNumber: String,
        invoiceText: String,
        imageByteArray: ByteArray,
        invoiceFooter: String,
    ): PrinterState {

        PrintUtil.resetPrint()

        //Process header
        val lines = invoiceText.lines()
        val header = lines[0].subSequence(7, lines[0].lastIndex - 7)
        PrintUtil.printText(
            PrintConfig.Align.ALIGN_CENTER,
            PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE,
            false,
            false,
            "$header\n",
        )

        //Process body
        for (i in 1..lines.lastIndex) {
            val (text, fontSize) = when (lines[i].first()) {
                '=' -> Pair(
                    lines[i].subSequence(1, lines.lastIndex - 1),
                    PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE
                )
                '-' -> Pair(lines[i], PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE)
                else -> Pair(lines[i].replace(" ", "  "), PrintConfig.FontSize.TOP_FONT_SIZE_XSMALL)
            }
            PrintUtil.printText(
                PrintConfig.Align.ALIGN_CENTER,
                fontSize,
                false,
                false,
                "$text\n",
            )
        }

        //process bitmap
        val bitmap = BitmapFactory.decodeByteArray(
            imageByteArray,
            0,
            imageByteArray.size,
            BitmapFactory.Options(),
        )

        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, 360, 360, true)
        PrintUtil.printBitmap(scaledBitmap)
        PrintUtil.printLine(1)

        //process footer
        PrintUtil.printText(
            PrintConfig.Align.ALIGN_CENTER,
            PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE,
            false,
            false,
            invoiceFooter.substring(6, invoiceFooter.lastIndex - 6),
        )
        PrintUtil.printLine(6)
        PrintUtil.start()

        return state
    }


    private fun formatInvoiceText(text: String): String {
        val stringBuilder = StringBuilder()
        val lines = text.lines()
        for (line in lines) {
//            val temp = line.r

//            stringBuilder.append("$paddedLine\n")
        }

        return text.replace("=", "-").replace(" ", "  ")
    }
}