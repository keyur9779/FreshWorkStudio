
package com.app.freshworkstudio.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class CoroutinesRule : TestRule, TestCoroutineScope by TestCoroutineScope() {

  private val testCoroutinesDispatcher = TestCoroutineDispatcher()
  private val testCoroutineScope = TestCoroutineScope(testCoroutinesDispatcher)

  override fun apply(base: Statement?, description: Description?) = object : Statement() {
    override fun evaluate() {
      Dispatchers.setMain(testCoroutinesDispatcher)
      base?.evaluate()
      Dispatchers.resetMain()
      testCoroutineScope.cleanupTestCoroutines()
    }
  }

  fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) {
    testCoroutineScope.runBlockingTest { block() }
  }
}
