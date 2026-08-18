package heap

import values.Value

/**
 * type ->
 * 1 -> CharacterValue('\u0000')
 * 2 -> BooleanValue(false)
 * 3 -> IntValue(0)
 * 4 -> RealValue(0.0)
 * 5 -> StringValue("")
 */
class MatrixObject(var lines: Int, var columns : Int, var type: Int, var values : kotlin.Array<Value?>) : HeapObject{
}