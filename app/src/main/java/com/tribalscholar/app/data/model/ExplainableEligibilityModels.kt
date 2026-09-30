package com.tribalscholar.app.data.model

enum class RuleResult(val label: String) {
    PASS("PASS"),
    NEEDS_REVIEW("NEEDS REVIEW"),
    FAIL("FAIL")
}

data class EligibilityRuleNode(
    val id: String,
    val ruleName: String,
    val conditionDescription: String,
    val evidenceDocument: String,
    val observedValue: String,
    val ruleVersion: String,
    val result: RuleResult,
    val explanation: String
)

data class ExplainableEligibilityGraph(
    val schemeId: String,
    val schemeTitle: String,
    val overallStatus: RuleResult,
    val ruleNodes: List<EligibilityRuleNode>,
    val whyEligibleSummary: String,
    val disclaimerText: String = "AI explains configured rules. Final decision is made by authorized officials."
)
