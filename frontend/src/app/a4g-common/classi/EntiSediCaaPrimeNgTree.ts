import { TreeNode } from "primeng/api";
import { SedeCaa, EnteCaa } from "./EnteSedeCaa";

export class SedeTreeNode implements TreeNode {
    constructor(public label: string, public data: SedeCaa) {
    }

    static fromEnteSede(sede: SedeCaa) {
        return new SedeTreeNode(sede.descrizione, sede);
    }
}

export class EnteTreeNode implements TreeNode {
    public partialSelected: boolean = false;

    constructor(public label: string, public data: number, public children: SedeTreeNode[]) {
    };

    public static getTreeNode(uc: EnteCaa): EnteTreeNode {
        let sediTreeNodes: SedeTreeNode[] = [];
        if (uc.sedi !== undefined) {
            for (let c of uc.sedi) {
                sediTreeNodes.push(SedeTreeNode.fromEnteSede(c));
            }
        }
        let entreTreeNode: EnteTreeNode = new EnteTreeNode(
            uc.descrizione, uc.id, sediTreeNodes);
        return entreTreeNode;
    }

    public static getTreeNodes(entiCaa: Array<EnteCaa>): EnteTreeNode[] {
        let entiCaaTreeNode: EnteTreeNode[] = [];
        entiCaa.sort((n1, n2) => {
            if (n1.descrizione > n2.descrizione) {
                return 1;
            }
            if (n1.descrizione < n2.descrizione) {
                return -1;
            }
            return 0;
        });
        for (let uf of entiCaa) {
            uf.sedi.sort((n1, n2) => {
                if (n1.descrizione > n2.descrizione) {
                    return 1;
                }
                if (n1.descrizione < n2.descrizione) {
                    return -1;
                }
                return 0;
            });
            entiCaaTreeNode.push(EnteTreeNode.getTreeNode(uf));
        }
        return entiCaaTreeNode;
    }

    public static selectEntiSedi(entiCaa: EnteTreeNode[], entiSediSelectedIds: number[]): TreeNode[] {
        let selectedNodes: TreeNode[] = [];
        if (entiSediSelectedIds) {
            entiCaa.forEach((enteNode, index) => {
                let selectedChildrenCount: number = 0;
                enteNode.children.forEach((childNode, index) => {
                    if (entiSediSelectedIds.find(p => p == (childNode.data.id as number).valueOf())) {
                        selectedNodes.push(childNode);
                        selectedChildrenCount++;
                    }
                });
                if (selectedChildrenCount > 0) {
                    if (selectedChildrenCount === enteNode.children.length) {
                        selectedNodes = selectedNodes.concat([enteNode], enteNode.children);
                    } else {
                        enteNode.partialSelected = true;
                    }
                }
            });
        }
        return selectedNodes;
    }
}