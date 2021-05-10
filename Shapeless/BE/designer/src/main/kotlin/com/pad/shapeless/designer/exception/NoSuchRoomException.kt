package com.pad.shapeless.designer.exception

import java.util.*

class NoSuchRoomException(roomId: UUID) : Exception("No such room $roomId exists!")