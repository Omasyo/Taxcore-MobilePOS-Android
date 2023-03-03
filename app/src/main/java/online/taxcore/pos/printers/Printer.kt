package online.taxcore.pos.printers

interface Printer {
    fun print(
        invoiceNumber: String,
        invoiceText: String = "",
        imageByteArray: ByteArray,
        invoiceFooter: String = "",
    ): PrinterState
}