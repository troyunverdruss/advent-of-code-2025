package dev.yort

import com.microsoft.z3.Context
import com.microsoft.z3.IntNum
import com.microsoft.z3.IntSort
import com.microsoft.z3.Status
import dev.yort.Day10.MachineSpec
import java.io.File

object Day10Part2Take4 {
    fun run() {
        val specs = File("inputs/Day10.txt").readLines().map { (MachineSpec.parseLine(it)) }

        val sumOfMinButtonPresses = specs.mapIndexed { index, spec ->
//            println("Starting on $index")
            solveMachineZ3(spec)
        }.sum()

        println("Day 10, Part 2: The sum of the min number of button presses to reach the required joltage state is [$sumOfMinButtonPresses]")
    }

    fun solveMachineZ3(spec: MachineSpec): Int {
        Context().let { c ->
            val opt = c.mkOptimize()
            val z3Buttons = spec.buttons.mapIndexed { index, _ ->
                val z3Button = c.mkIntConst("button${index}")
                opt.Add(c.mkGe(z3Button, c.mkInt(0)))
                z3Button
            }
            val z3Joltages = spec.joltages.mapIndexed { specJoltageIndex, specJoltageTarget ->
                val z3Joltage = c.mkIntConst("joltage${specJoltageIndex}")

                val z3ButtonsThatAffectJoltage = spec.buttons
                    .mapIndexed { specButtonIndex, specButton ->
                        Pair(specButtonIndex, specButton)
                    }.filter { it.second.contains(specJoltageIndex.toLong()) }
                    .map { z3Buttons[it.first] }

                opt.Add(
                    c.mkEq(
                        z3Joltage, c.mkAdd(*z3ButtonsThatAffectJoltage.toTypedArray())
                    )
                )

                opt.Add(c.mkEq(z3Joltage, c.mkInt(specJoltageTarget)))
                z3Joltage
            }

            opt.MkMinimize(c.mkAdd(*z3Buttons.toTypedArray()))

            val status = opt.Check()

            // Solve
            if (status == Status.SATISFIABLE) {
                val model = opt.model
//                z3Joltages.forEachIndexed { index, j ->
//                    println("Joltage ${index} = ${model.evaluate(j, false)}")
//                }
//                z3Buttons.forEachIndexed { index, b ->
//                    println("Button ${index} = ${model.evaluate(b, false)}")
//                }
                val totalPresses = (model.eval(c.mkAdd(*z3Buttons.toTypedArray()), false) as IntNum).int
//                println("Total button presses: $totalPresses")
                return totalPresses
            } else {
                println("Status: $status")
            }
            error("unsolvable")
        }
    }

    fun solveExample1MachineZ3Testing(spec: MachineSpec): Int {
        val ctx = Context()
        val opt = ctx.mkOptimize()

        // Define my buttons
        val button1 = ctx.mkIntConst("button1") // (3)
        val button2 = ctx.mkIntConst("button2") // (1,3)
        val button3 = ctx.mkIntConst("button3") // (2)
        val button4 = ctx.mkIntConst("button4") // (2,3)
        val button5 = ctx.mkIntConst("button5") // (0,2)
        val button6 = ctx.mkIntConst("button6") // (0,1)

        // Button counts must be 0 or more
        opt.Add(ctx.mkGe(button1, ctx.mkInt(0)))
        opt.Add(ctx.mkGe(button2, ctx.mkInt(0)))
        opt.Add(ctx.mkGe(button3, ctx.mkInt(0)))
        opt.Add(ctx.mkGe(button4, ctx.mkInt(0)))
        opt.Add(ctx.mkGe(button5, ctx.mkInt(0)))
        opt.Add(ctx.mkGe(button6, ctx.mkInt(0)))

        // Joltage registers
        val j0 = ctx.mkIntConst("0")
        val j1 = ctx.mkIntConst("1")
        val j2 = ctx.mkIntConst("2")
        val j3 = ctx.mkIntConst("3")

        // Which buttons affect the joltages?
        opt.Add(ctx.mkEq(j0, ctx.mkAdd<IntSort>(button5, button6)))
        opt.Add(ctx.mkEq(j1, ctx.mkAdd(button2, button6)))
        opt.Add(ctx.mkEq(j2, ctx.mkAdd(button3, button4, button5)))
        opt.Add(ctx.mkEq(j3, ctx.mkAdd(button1, button2, button4)))

        // Set the goals for each joltage
        opt.Add(ctx.mkEq(j0, ctx.mkInt(3)))
        opt.Add(ctx.mkEq(j1, ctx.mkInt(5)))
        opt.Add(ctx.mkEq(j2, ctx.mkInt(4)))
        opt.Add(ctx.mkEq(j3, ctx.mkInt(7)))

        // Identify that the goal is to minimize all the buttons
        opt.MkMinimize(ctx.mkAdd(button1, button2, button3, button4, button5, button6))

        // Check that it's satisfiable
        val status = opt.Check()

        // Solve
        if (status == Status.SATISFIABLE) {
            val model = opt.model
            println("j0 = ${model.evaluate(j0, false)}")
            println("j1 = ${model.evaluate(j1, false)}")
            println("j2 = ${model.evaluate(j2, false)}")
            println("j3 = ${model.evaluate(j3, false)}")
            println("Button 1: ${model.evaluate(button1, false)} times")
            println("Button 2: ${model.evaluate(button2, false)} times")
            println("Button 3: ${model.evaluate(button3, false)} times")
            println("Button 4: ${model.evaluate(button4, false)} times")
            println("Button 5: ${model.evaluate(button5, false)} times")
            println("Button 6: ${model.evaluate(button6, false)} times")
            println("Total: ${model.evaluate(ctx.mkAdd(button1, button2, button3, button4, button5, button6), false)}")
            return (model.evaluate(ctx.mkAdd(button1, button2, button3, button4, button5, button6), false) as IntNum).int
        } else {
            println("Status: $status")
        }
        error("unsolvable")
    }

    fun solveMachineZ3Testing(spec: MachineSpec): Int {
        val ctx = Context()
        val opt = ctx.mkOptimize()

        // Define my buttons
        val button1 = ctx.mkIntConst("button1") // (0,1)
        val button2 = ctx.mkIntConst("button2") // (1)
        val button3 = ctx.mkIntConst("button3") // (0,2)

        // Button counts must be 0 or more
        opt.Add(ctx.mkGe(button1, ctx.mkInt(0)))
        opt.Add(ctx.mkGe(button2, ctx.mkInt(0)))
        opt.Add(ctx.mkGe(button3, ctx.mkInt(0)))

        // Joltage registers
        val j0 = ctx.mkIntConst("0")
        val j1 = ctx.mkIntConst("1")
        val j2 = ctx.mkIntConst("2")

        // Which buttons affect the joltages?
        // j0 is affected by both button1 and button3
        opt.Add(ctx.mkEq(j0, ctx.mkAdd(button1, button3)))
        // j1 is affected by both button1 and button2
        opt.Add(ctx.mkEq(j1, ctx.mkAdd(button1, button2)))
        // j2 is only affected by button3
        opt.Add(ctx.mkEq(j2, button3))

        // Set the goals for each joltage
        opt.Add(ctx.mkEq(j0, ctx.mkInt(2)))
        opt.Add(ctx.mkEq(j1, ctx.mkInt(5)))
        opt.Add(ctx.mkEq(j2, ctx.mkInt(1)))

        // Identify that the goal is to minimize all the buttons
        opt.MkMinimize(ctx.mkAdd(button1, button2, button3))

        // Check that it's satisfiable
        val status = opt.Check()

        // Solve
        if (status == Status.SATISFIABLE) {
            val model = opt.model
            println("x = ${model.evaluate(j0, false)}")
            println("y = ${model.evaluate(j1, false)}")
            println("z = ${model.evaluate(j2, false)}")
            println("Operation 1 (x+1, y+1): ${model.evaluate(button1, false)} times")
            println("Operation 2 (y+1): ${model.evaluate(button2, false)} times")
            println("Operation 3 (x+1, z+1): ${model.evaluate(button3, false)} times")
            println("Total: ${model.evaluate(ctx.mkAdd(j0, j1, j2), false)}")
        } else {
            println("Status: $status")
        }

//        val a = ctx.mkIntConst("a")
//        val b = ctx.mkIntConst("b")
//        val eq = ctx.mkEq(
//            ctx.mkAdd(ctx.mkMul(a, ctx.mkInt(5)), ctx.mkMul(b, ctx.mkInt(2))),
//            ctx.mkInt(10)
//        )
//        opt.Add(eq)
//        val objective =opt.MkMinimize(ctx.mkAdd(a,b))
//
//        val status = opt.Check()
//
//        if (status == Status.SATISFIABLE) {
//            val model = opt.model
//            println("a = ${model.evaluate(a, false)}")
//            println("b = ${model.evaluate(b, false)}")
//            println("a+b = ${model.evaluate(ctx.mkAdd(a, b), false)}")
//            println("Optimized value: ${objective.upper}") // The optimal value
//        } else {
//            println("Status: $status")
//        }


//
//        // "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}"
//
//        // Set constraints.
////        val aExp = ctx.mkIntConst("a") // (3)
////        val bExp = ctx.mkIntConst("b") // (1,3)
////        val cExp = ctx.mkIntConst("c") // (2)
////        val dExp = ctx.mkIntConst("d") // (2,3)
////        val eExp = ctx.mkIntConst("e") // (0,2)
////        val fExp = ctx.mkIntConst("f") // (0,1)
//
//        val coef1 = ctx.mkIntConst("coef1") // (3)
//        val coef2 = ctx.mkIntConst("coef2") // (1,3)
//        val coef3 = ctx.mkIntConst("coef3") // (2)
//        val coef4 = ctx.mkIntConst("coef4") // (2,3)
//        val coef5 = ctx.mkIntConst("coef5") // (0,2)
//        val coef6 = ctx.mkIntConst("coef6") // (0,1)
//
//
//        val vExp = ctx.mkIntConst("v") // 0
//        val wExp = ctx.mkIntConst("w") // 1
//        val xExp = ctx.mkIntConst("x") // 2
//        val yExp = ctx.mkIntConst("y") // 3
////        val zExp = ctx.mkIntConst("z")
//
////        ctx.mkMul<>()
//// ctx.mkArray
////        val eq1 = ctx.mkEq(aExp, yExp)
////        val eq2 = ctx.mkEq(bExp, ctx.mkAdd(wExp, yExp))
////        val eq3 = ctx.mkEq(cExp, xExp)
////        val eq4 = ctx.mkEq(dExp, ctx.mkAdd(xExp, yExp))
////        val eq5 = ctx.mkEq(eExp, ctx.mkAdd(vExp, xExp))
////        val eq6 = ctx.mkEq(fExp, ctx.mkAdd(vExp, wExp))
//
////        opt.Add(eq1)
////        opt.Add(eq2)
////        opt.Add(eq3)
////        opt.Add(eq4)
////        opt.Add(eq5)
////        opt.Add(eq6)
//
//        val joltage1 = ctx.mkEq(
//            ctx.mkAdd(
//                ctx.mkMul(coef1, yExp),
//                ctx.mkAdd(ctx.mkMul(coef2, wExp), ctx.mkMul(coef2, yExp)),
//                ctx.mkMul(coef3, xExp),
//                ctx.mkAdd(ctx.mkMul(coef4, xExp), ctx.mkMul(coef4, yExp)),
//                ctx.mkAdd(ctx.mkMul(coef5, vExp), ctx.mkMul(coef5, xExp)),
//                ctx.mkAdd(ctx.mkMul(coef6, vExp), ctx.mkMul(coef6, wExp)),
//            ),
//            ctx.mkEq(vExp,ctx.mkInt(3)))
//
//        val joltage2 = ctx.mkEq(
//            ctx.mkAdd(
//                ctx.mkMul(coef1, yExp),
//                ctx.mkAdd(ctx.mkMul(coef2, wExp), ctx.mkMul(coef2, yExp)),
//                ctx.mkMul(coef3, xExp),
//                ctx.mkAdd(ctx.mkMul(coef4, xExp), ctx.mkMul(coef4, yExp)),
//                ctx.mkAdd(ctx.mkMul(coef5, vExp), ctx.mkMul(coef5, xExp)),
//                ctx.mkAdd(ctx.mkMul(coef6, vExp), ctx.mkMul(coef6, wExp)),
//            ),
//            ctx.mkEq(wExp,ctx.mkInt(5)))
//
//        val joltage3 = ctx.mkEq(
//            ctx.mkAdd(
//                ctx.mkMul(coef1, yExp),
//                ctx.mkAdd(ctx.mkMul(coef2, wExp), ctx.mkMul(coef2, yExp)),
//                ctx.mkMul(coef3, xExp),
//                ctx.mkAdd(ctx.mkMul(coef4, xExp), ctx.mkMul(coef4, yExp)),
//                ctx.mkAdd(ctx.mkMul(coef5, vExp), ctx.mkMul(coef5, xExp)),
//                ctx.mkAdd(ctx.mkMul(coef6, vExp), ctx.mkMul(coef6, wExp)),
//            ),
//            ctx.mkEq(xExp,ctx.mkInt(7)))
//
//        val joltage4 = ctx.mkEq(
//            ctx.mkAdd(
//                ctx.mkMul(coef1, yExp),
//                ctx.mkAdd(ctx.mkMul(coef2, wExp), ctx.mkMul(coef2, yExp)),
//                ctx.mkMul(coef3, xExp),
//                ctx.mkAdd(ctx.mkMul(coef4, xExp), ctx.mkMul(coef4, yExp)),
//                ctx.mkAdd(ctx.mkMul(coef5, vExp), ctx.mkMul(coef5, xExp)),
//                ctx.mkAdd(ctx.mkMul(coef6, vExp), ctx.mkMul(coef6, wExp)),
//            ),
//            ctx.mkEq(yExp,ctx.mkInt(7)))
//
//        opt.Add(joltage1)
//        opt.Add(joltage2)
//        opt.Add(joltage3)
//        opt.Add(joltage4)
//
//
////        opt.Add(ctx.mkEq(vExp, ctx.mkInt(3)))
////        opt.Add(ctx.mkEq(wExp, ctx.mkInt(5)))
////        opt.Add(ctx.mkEq(xExp, ctx.mkInt(4)))
////        opt.Add(ctx.mkEq(yExp, ctx.mkInt(7)))
//
//        val result = opt.MkMinimize(
//            ctx.mkAdd(
//                coef1,
//                coef2,
//                coef3,
//                coef4,
//                coef5,
//                coef6,
//            )
//        )
//        println(opt.Check())
//        println(result)
//
//        opt.Add(
//            ctx.mkEq(
//                ctx.mkAdd<IntSort?>(xExp, yExp), ctx.mkInt(11)
//            ),
//        )
//
//        opt.Add(
//            ctx.mkGe(xExp, ctx.mkInt(0)),
//        )
//
//        opt.Add(
//            ctx.mkGe(yExp, ctx.mkInt(5))
//        )
//
//        // Set objectives.
//        val mx: Optimize.Handle<*>? = opt.MkMaximize<IntSort?>(xExp)
//        val my: Optimize.Handle<*>? = opt.MkMaximize<IntSort?>(yExp)
//
//        println(opt.Check())
//        println(mx)
//        println(my)

        ctx.close()
        return 0
    }
}
