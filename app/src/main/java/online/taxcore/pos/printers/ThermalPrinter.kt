package online.taxcore.pos.printers

interface ThermalPrinter {
    fun print(
        invoiceNumber: String,
        invoiceText: String = "",
        imageByteArray: ByteArray,
        invoiceFooter: String = "",
    )
}