package org.teamsoft.metaheuristics.util;

import metaheuristics.generators.GeneratorType;
import org.teamsoft.entity.Worker;
import problem.definition.State;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectRoleState extends State {

    public ProjectRoleState(List<Object> code, List<Double> evaluation, GeneratorType typeGenerator) {
    }

    @Override
    public ProjectRoleState clone() {
//        super.clone();
        return new ProjectRoleState(this.code, this.evaluation, this.typeGenerator);
    }

    public ProjectRoleState(ArrayList<Object> code, List<Double> evaluation, GeneratorType typeGenerator) {
        //Agregando los valores del map en una copia de ProblemStat
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < code.size(); i++) {
            ProjectRole projectRole = (ProjectRole) code.get(i);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
            List<RoleWorker> copyRoleWorkers = new ArrayList<>(roleWorkers.size());

            for (int j = 0; j < roleWorkers.size(); j++) {
                RoleWorker roleWorker = roleWorkers.get(j);
                List<Worker> workers = roleWorker.getWorkers();
                List<Worker> copyWorkers = new ArrayList<>(roleWorker.getWorkers().size());

                for (int k = 0; k < workers.size(); k++) {
                    Worker copyWorker = workers.get(k);
                    copyWorkers.add(copyWorker);
                }
                RoleWorker copyRoleworker = new RoleWorker(roleWorker.getRole(), copyWorkers, roleWorker.getNeededWorkers(), roleWorker.getFixedWorkers());
                copyRoleWorkers.add(copyRoleworker);
            }
            Double[] projectEval = new Double[12];
            for (int j = 0; j < projectRole.getProjectEvaluation().length; j++) {
                projectEval[j] = projectRole.getProjectEvaluation()[j];
            }
            ProjectRole copyProjectRole = new ProjectRole(projectRole.getProject(), projectEval, copyRoleWorkers);
            list.add(copyProjectRole);
        }
        this.code = list;
        if (evaluation != null) {
            this.evaluation = new ArrayList<>(evaluation);
        } else {
            this.evaluation = new ArrayList<>();
        }
        this.typeGenerator = typeGenerator;
    }

    public ProjectRoleState() {
        super();
    }

    public ProjectRoleState(ArrayList<Object> code) {
        super(code);
    }

    public ProjectRoleState(State ps) {
        super(ps);
    }

    @Override
    public void setCode(ArrayList<Object> code) {
        ArrayList<Object> list = new ArrayList<>();
        for (int i = 0; i < code.size(); i++) {
            ProjectRole projectRole = (ProjectRole) code.get(i);
            List<RoleWorker> roleWorkers = projectRole.getRoleWorkers();
            List<RoleWorker> copyRoleWorkers = new ArrayList<>(roleWorkers.size());

            for (int j = 0; j < roleWorkers.size(); j++) {
                RoleWorker roleWorker = roleWorkers.get(j);
                List<Worker> workers = roleWorker.getWorkers();
                List<Worker> copyWorkers = new ArrayList<>(roleWorker.getWorkers().size());

                for (int k = 0; k < workers.size(); k++) {
                    Worker copyWorker = workers.get(k);
                    copyWorkers.add(copyWorker);
                }
                RoleWorker copyRoleworker = new RoleWorker(roleWorker.getRole(), copyWorkers, roleWorker.getNeededWorkers(), roleWorker.getFixedWorkers());
                copyRoleWorkers.add(copyRoleworker);
            }
            ProjectRole copyProjectRole = new ProjectRole(projectRole.getProject(), projectRole.getProjectEvaluation(), copyRoleWorkers);
            list.add(copyProjectRole);
        }
        this.code = list;
    }

//        @Override
//	public double Distance(State state){ 
//		int distance = 0;
//		for (int i = 0; i < this.getCode().size(); i++) {
//			ProjectRole projectRoleA = (ProjectRole)this.getCode().get(i);
//			ProjectRole projectRoleB = (ProjectRole)state.getCode().get(i);
//			for (Iterator iterator2 = projectRoleA.getRoleWorkers().iterator(); iterator2.hasNext();) {
//				RoleWorker roleWorkerA = (RoleWorker) iterator2.next();
//				for (Iterator iterator1 = projectRoleB.getRoleWorkers().iterator(); iterator1.hasNext();) {
//					RoleWorker roleWorkerB = (RoleWorker) iterator1.next();
//					for (Iterator iterator = roleWorkerB.getWorkers().iterator(); iterator.hasNext();) {
//						                                      Worker worker = (Worker) iterator.next();
//						if(!roleWorkerA.getWorkers().contains(worker)){
//							distance++;
//						}
//					}
//				}
//			}
//		}
//		return distance;
//	}
    
    
        @Override
	public boolean Comparator(State state){
            boolean found = false;
            int length = this.getCode().size();
            int count = 0;
		for (int j = 0; j < length; j++) {
			List<RoleWorker> roleWorkers = ((ProjectRole)state.getCode().get(j)).getRoleWorkers();
			int totalRoles = ((ProjectRole)state.getCode().get(j)).getRoleWorkers().size();
			int countRoles = 0;
			for (int i = 0; i < totalRoles; i++) {
				for (Iterator iterator = roleWorkers.iterator(); iterator.hasNext();) {
					RoleWorker roleWorker = (RoleWorker) iterator.next();
					int countWorkers = 0;
					for (int k = 0; k < roleWorker.getWorkers().size(); k++) {
					    if(roleWorker.getWorkers().size()==((ProjectRole)this.getCode().get(j)).getRoleWorkers().get(i).getWorkers().size()) {
                            Worker thisWorker = ((ProjectRole) this.getCode().get(j)).getRoleWorkers().get(i).getWorkers().get(k);
                            if (((Worker) roleWorker.getWorkers().toArray()[k]).getId().equals(thisWorker.getId())) {
                                countWorkers++;
                            }
                        }
					}
					if(countWorkers == roleWorker.getWorkers().size()){
						countRoles++;
					}
				}
			}
			if(countRoles == totalRoles){
				count++;
			}
		}
		if(count == length){
		    found = true;
		}
		return found;
	}
}
