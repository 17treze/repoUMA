/**
 * 
 */
package it.tndigitale.a4g.ags.dto;

import java.math.BigDecimal;

/**
 * @author A2AC0147
 *
 */
public class DatiMovimentoAgs {

	private BigDecimal workflowId;
	private BigDecimal groupId;
	private String scoStatusFrom;
	private String scoTransitionTo;
	private String scoStatusTo;
	private BigDecimal ruleId;
	private BigDecimal taskIdTo;
	private BigDecimal actTypeId;

	public BigDecimal getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(BigDecimal workflowId) {
		this.workflowId = workflowId;
	}

	public BigDecimal getGroupId() {
		return groupId;
	}

	public void setGroupId(BigDecimal groupId) {
		this.groupId = groupId;
	}

	public String getScoStatusFrom() {
		return scoStatusFrom;
	}

	public void setScoStatusFrom(String scoStatusFrom) {
		this.scoStatusFrom = scoStatusFrom;
	}

	public String getScoTransitionTo() {
		return scoTransitionTo;
	}

	public void setScoTransitionTo(String scoTransitionTo) {
		this.scoTransitionTo = scoTransitionTo;
	}

	public String getScoStatusTo() {
		return scoStatusTo;
	}

	public void setScoStatusTo(String scoStatusTo) {
		this.scoStatusTo = scoStatusTo;
	}

	public BigDecimal getRuleId() {
		return ruleId;
	}

	public void setRuleId(BigDecimal ruleId) {
		this.ruleId = ruleId;
	}

	public BigDecimal getTaskIdTo() {
		return taskIdTo;
	}

	public void setTaskIdTo(BigDecimal taskIdTo) {
		this.taskIdTo = taskIdTo;
	}

	public BigDecimal getActTypeId() {
		return actTypeId;
	}

	public void setActTypeId(BigDecimal actTypeId) {
		this.actTypeId = actTypeId;
	}

}
