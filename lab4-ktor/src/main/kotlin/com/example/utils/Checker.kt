package com.example.utils

import com.example.dto.DotDto
import com.example.dto.ResultDto
import java.time.LocalDateTime
import kotlin.math.abs

class Checker {
    fun post(dot : DotDto) : ResultDto{

        val x = dot.x
        val y = dot.y
        when(val r = dot.r){
            0.0 ->  {
                return ResultDto(
                    x,
                    y,
                    r,
                    dot.id,
                    false,
                    LocalDateTime.now().toString()
                )
            }
            else -> {
                if (r > 0){
                    if (x >= 0 && y >= 0){
                        return ResultDto(
                            x,
                            y,
                            r,
                            dot.id,
                            checkFirst(x, y, r),
                            LocalDateTime.now().toString()
                        )
                    } else if (x <= 0 && y <=0){
                        return ResultDto(
                            x,
                            y,
                            r,
                            dot.id,
                            checkThird(x, y, r),
                            LocalDateTime.now().toString()
                        )
                    } else if (x<=0 && y>=0){
                        return ResultDto(
                            x,
                            y,
                            r,
                            dot.id,
                            checkSecond(x, y, r),
                            LocalDateTime.now().toString()
                        )
                    }else{
                        return ResultDto(
                            x,
                            y,
                            r,
                            dot.id,
                            false,
                            LocalDateTime.now().toString()
                        )
                    }
                }
                else {
                    if (x >= 0 && y >= 0){
                        return ResultDto(
                            x,
                            y,
                            r,
                            dot.id,
                            checkThird(x, y, r),
                            LocalDateTime.now().toString()
                        )
                    } else if (x <= 0 && y <=0){
                        return ResultDto(
                            x,
                            y,
                            r,
                            dot.id,
                            checkFirst(x, y, r),
                            LocalDateTime.now().toString()
                        )
                    } else if (x<=0 && y>=0){
                        return ResultDto(
                            x,
                            y,
                            r,
                            dot.id,
                            false,
                            LocalDateTime.now().toString()
                        )
                    }else{
                        return ResultDto(
                            x,
                            y,
                            r,
                            dot.id,
                            checkSecond(x, y, r),
                            LocalDateTime.now().toString()
                        )
                    }
                }
            }
        }
    }
    private fun checkFirst(x : Double, y : Double, r : Double) : Boolean{
        return (abs(x) <= abs(r/2) && abs(y) <= abs(r))
    }
    private fun checkSecond(x : Double, y : Double, r : Double) : Boolean{
        return if (r>0){
            (y-2*x-r <= 0)
        }else{
            (y-2*x-r >= 0)
        }

    }
    private fun checkThird(x : Double, y : Double, r : Double) : Boolean{
        return (x*x + y*y <= (r/2)*(r/2))
    }
}
