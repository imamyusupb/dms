package seino.indomobil.dmsmobile.presentation.utils

import java.text.SimpleDateFormat
import java.util.*

class DateFormat {
    fun dateFormat(date:String?) :String{
        return if (date.isNullOrEmpty()) ""
        else{
            val currentFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val dateParse = currentFormat.parse(date)
            val toFormat = SimpleDateFormat("dd, MMM yyyy",Locale.getDefault())
            toFormat.format(dateParse)
        }
    }
}