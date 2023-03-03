package online.taxcore.pos.printers

interface Printer {
    val ERROR: Int
        get() = 100

    fun print(
        invoiceNumber: String,
        invoiceText: String = "",
        imageByteArray: ByteArray,
        invoiceFooter: String = "",
    ): Int
}