package com.bigbratan.emulair.common.utils.kotlin

fun Long.kiloBytes(): Long = this * 1000L

fun Long.megaBytes(): Long = this.kiloBytes() * 1000L

fun Long.gigaBytes(): Long = this.megaBytes() * 1000L
