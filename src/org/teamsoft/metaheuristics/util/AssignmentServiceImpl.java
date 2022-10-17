/**
 * This is only generated once! It will never be overwritten.
 * You can (and have to!) safely modify it by hand.
 */
package org.teamsoft.metaheuristics.util;

/**
 * @see org.andromda.teamsoft.services.AssignmentService
 */
public class AssignmentServiceImpl
//extends org.andromda.teamsoft.services.AssignmentServiceBase
{
//	
//    public static 	LinkedList<Object> lista;
//	
//
//
//	/**
//	 * @see org.andromda.teamsoft.services.AssignmentService#ActionGtMentalSocial(java.lang.Long[])
//	 */
//	@SuppressWarnings("unchecked")
//	protected java.lang.Boolean handleActionGtMentalSocial(java.lang.Long[] workers)
//	throws java.lang.Exception
//	{
//		Collection rolesBelbin = getRoleBelbinDao().loadAll();
//		Collection workersList = new LinkedList();
//		for(int i=0; i<workers.length;i++)
//		{
//			workersList.add(getWorkerDao().load(workers[i]));
//		}
//
//		Long countActionRoles = 0L;
//		Long countMentalRoles = 0L;
//		Long countSocialRoles = 0L;
//
//		for(Iterator iter = rolesBelbin.iterator();iter.hasNext();)
//		{
//			RoleBelbin roleBelbin = (RoleBelbin) iter.next();
//
//			for(Iterator workersIter = workersList.iterator(); workersIter.hasNext();)
//			{
//				Worker worker = (Worker)workersIter.next();
//				if(worker.getRolesBelbin().contains(roleBelbin))
//				{
//					if(roleBelbin.getCategory().equals("Rol de Acci�n"))
//					{
//						countActionRoles++;
//					}
//					else
//						if(roleBelbin.getCategory().equals("Rol Mental"))
//						{
//							countMentalRoles++;
//						}
//						else
//							if(roleBelbin.getCategory().equals("Rol Social"))
//							{
//								countSocialRoles++;
//							}
//				}
//			}
//		}
//		return ((countActionRoles>countMentalRoles) &&(countMentalRoles>countSocialRoles))?true:false;
//	}
//	/**
//	 * @see org.andromda.teamsoft.services.AssignmentService#ExistAllBelbinInTeam(java.lang.Long[])
//	 */
//	@SuppressWarnings("unchecked")
//	protected java.lang.Boolean handleExistAllBelbinInTeam(java.lang.Long[] workers)
//	throws java.lang.Exception
//	{
//		Collection rolesBelbin = getRoleBelbinDao().loadAll();
//		Collection workersList = new LinkedList();
//		for(int i=0; i<workers.length;i++)
//		{
//			workersList.add(getWorkerDao().load(workers[i]));
//		}
//
//		Long countActionRoles = 0L;
//		Long countMentalRoles = 0L;
//		Long countSocialRoles = 0L;
//
//		for(Iterator iter = rolesBelbin.iterator();iter.hasNext();)
//		{
//			RoleBelbin roleBelbin = (RoleBelbin) iter.next();
//
//			for(Iterator workersIter = workersList.iterator(); workersIter.hasNext();)
//			{
//				Worker worker = (Worker)workersIter.next();
//				if(worker.getRolesBelbin().contains(roleBelbin))
//				{
//					if(roleBelbin.getCategory().equals("Rol de Acci�n"))
//					{
//						countActionRoles++;
//					}
//					else
//						if(roleBelbin.getCategory().equals("Rol Mental"))
//						{
//							countMentalRoles++;
//						}
//						else
//							if(roleBelbin.getCategory().equals("Rol Social"))
//							{
//								countSocialRoles++;
//							}
//				}
//			}
//		}
//		return (countActionRoles > 0L && countMentalRoles > 0L && countSocialRoles > 0L)?true:false;
//	}
//
//	/**
//	 * @see org.andromda.teamsoft.services.AssignmentService#RoleBelbinInWorkerAndRole(java.lang.Long, java.lang.Long)
//	 */
//	protected java.lang.Boolean handleRoleBelbinInWorkerAndRole(java.lang.Long idWorker, java.lang.Long idRole)
//	throws java.lang.Exception
//	{
//		Worker worker = getWorkerDao().load(idWorker);
//		Role role = getRoleDao().load(idRole);
//		boolean exist = false;
//		Collection rolesBelbin = role.getRoleBelbin();
//		Iterator iter = rolesBelbin.iterator();
//
//		while(iter.hasNext() && !exist)
//		{
//			RoleBelbin roleBelbin = (RoleBelbin)iter.next();
//			if(worker.getRolesBelbin().contains(roleBelbin))
//			{
//				exist = true;
//			}
//		}
//		return exist;
//	}
//
//	@Override
//
//	//Zmin del rol
//	protected Long handleCompetenceIndex(Long[] genericCompLevels,
//			Long[] genericCompImportances, Long[] technicalCompLevels,
//			Long[] technicalCompImportances) throws Exception {
//
//
//
//
//		Long index = 0L;
//		for(int i=0; i<genericCompLevels.length; i++)
//		{
//			index += getCompetitionLevelDao().load(genericCompLevels[i]).getLevels().getLevels() * getCompImportanceDao().load(genericCompImportances[i]).getLevels();
//		}
//
//
//		for(int i=0; i<technicalCompLevels.length; i++)
//		{
//			index += getTechnicalLevelDao().load(technicalCompLevels[i]).getLevels().getLevels() * getCompImportanceDao().load(technicalCompImportances[i]).getLevels();
//		}
//		return index;
//	}
//
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void handleCheckCompetitions(Long idProject) throws Exception {
//
//		Project project = getProjectDao().load(idProject);
//		Collection projectRolesCollection = project.getProjectRoles();
//
//		for(Iterator iter = projectRolesCollection.iterator(); iter.hasNext();)
//		{
//			ProjectRoles projectRoles = (ProjectRoles) iter.next();
//
//			//competencias Genericas
//			for(Iterator iRoleComp = projectRoles.getRole().getRoleCompetition().iterator(); iRoleComp.hasNext();)
//			{
//				RoleCompetition roleCompetition = (RoleCompetition)iRoleComp.next();
//				GenericCompImportance genCompImportance = (GenericCompImportance)ServiceLocator.instance().getCompetitionImportanceService().findGenericCompImportance(idProject, projectRoles.getRole().getId(), roleCompetition.getLevels().getGenCompetition().getId());
//				if(genCompImportance==null)
//				{
//					GenericCompImportance genericCompImportance = GenericCompImportance.Factory.newInstance(projectRoles.getRole(), roleCompetition.getCompImportance(), project, roleCompetition.getLevels());
//					getGenericCompImportanceDao().create(genericCompImportance);
//					project.getGenericCompImportance().add(genericCompImportance);
//				}
//			}
//
//
//			//competencias tecnicas
//			for(Iterator iRoleTecComp = projectRoles.getRole().getRoleTechCompetition().iterator(); iRoleTecComp.hasNext();)
//			{
//				RoleTechCompetition roleTecCompetition = (RoleTechCompetition)iRoleTecComp.next();
//
//				for(Iterator iTechComps = project.getTechnicalCompetitions().iterator(); iTechComps.hasNext();)
//				{
//					TechnicalCompetition technicalCompetition = (TechnicalCompetition)iTechComps.next();
//
//					if(technicalCompetition.getTechnicalCompetitionType().equals(roleTecCompetition.getLevels().getTechnicalCompetitionType()))
//					{
//						TechCompImp technicalCompImportance = (TechCompImp)ServiceLocator.instance().getCompetitionImportanceService().findTechnicalCompImportance(idProject, roleTecCompetition.getRoles().getId(), technicalCompetition.getId());
//						if(technicalCompImportance==null)
//						{
//							technicalCompImportance = TechCompImp.Factory.newInstance(roleTecCompetition.getCompImportance(), roleTecCompetition.getRoles(), project, technicalCompetition, roleTecCompetition.getLevels());
//							getTechCompImpDao().create(technicalCompImportance);
//							project.getTechnicalCompImportance().add(technicalCompImportance);
//						}
//					}
//				}
//			}
//		}
//	}
//
//	@Override
//	protected Collection handleGetObjectiveFunctions(Map additionalParameters)
//	throws Exception {
//		LinkedList<ObjetiveFunction> objectiveFunctions = new LinkedList<ObjetiveFunction>();
//		if(((Boolean)additionalParameters.get("maxCompetence"))==true)
//		{
//			MaximizeCompetency maxCompetence = new MaximizeCompetency();
//			maxCompetence.setParameters(additionalParameters);
//			if(additionalParameters.get("solutionMethod").equals(TypeSolutionMethod.FactoresPonderados))
//			{
//				maxCompetence.setWeight(((Float)additionalParameters.get("priorityCompetence")).floatValue());
//			}
//			objectiveFunctions.add(maxCompetence);
//		}
//
//		if(((Boolean)additionalParameters.get("minWorkload"))==true)
//		{
//			MinimizeWorkload minWorkLoad = new MinimizeWorkload();
//			minWorkLoad.setAdditionalParameters(additionalParameters);
//			if(additionalParameters.get("solutionMethod").equals(TypeSolutionMethod.FactoresPonderados))
//			{
//				minWorkLoad.setWeight(((Float)additionalParameters.get("priorityWorkload")).floatValue());
//			}
//			objectiveFunctions.add(minWorkLoad);
//		}
//
//		if(((Boolean)additionalParameters.get("minIncomp"))==true)
//		{
//			MinimizeIncompatibilities minIncomp = new MinimizeIncompatibilities();
//			if(additionalParameters.get("solutionMethod").equals(TypeSolutionMethod.FactoresPonderados))
//			{
//				minIncomp.setWeight(((Float)additionalParameters.get("prioritySynergy")).floatValue());
//			}
//			objectiveFunctions.add(minIncomp);
//		}
//
//		if(((Boolean)additionalParameters.get("minCost"))==true)
//		{
//			MinimizeCostDistance minCostDistance = new MinimizeCostDistance();
//			minCostDistance.setAdditionalParameters(additionalParameters);
//			if(additionalParameters.get("solutionMethod").equals(TypeSolutionMethod.FactoresPonderados))
//			{
//				minCostDistance.setWeight(((Float)additionalParameters.get("priorityCost")).floatValue());
//			}
//			objectiveFunctions.add(minCostDistance);
//		}
//		return objectiveFunctions;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleGetRestrictions(Map additionalParameters)
//	throws Exception {
//		LinkedList restrictions = new LinkedList();
//		restrictions.add(new AllCompetitionLevels());
//		restrictions.addFirst(new WorkerNotRepeatedInSameRole());
//		restrictions.add(new MaximumRoles());
//		restrictions.add(new IncompatibleRoles());
//		restrictions.add(new WorkerRemovedFromRole());
//
//		//restrictions for project boss
//		if(additionalParameters.get("isProjectBoss")!=null && ((Boolean)additionalParameters.get("isProjectBoss"))==true)
//			restrictions.add(new IsProjectBoss());
//		if(additionalParameters.get("bossAssigned")!=null && ((Boolean)additionalParameters.get("bossAssigned"))==true)
//			restrictions.add(new IsWorkerAssigned());
//		if(additionalParameters.get("correspondenceRolesBelbin")!=null && ((Boolean)additionalParameters.get("correspondenceRolesBelbin"))==true)
//			restrictions.add(new IsISCO());
//		if(additionalParameters.get("correspondenceRolesMB")!=null && ((Boolean)additionalParameters.get("correspondenceRolesMB"))==true)
//			restrictions.add(new IsEJ());
//		if(additionalParameters.get("domainExperience")!=null && ((Boolean)additionalParameters.get("domainExperience"))==true)
//			restrictions.add(new IsDomainExperience());
//		if(additionalParameters.get("complexityExperience")!=null && ((Boolean)additionalParameters.get("complexityExperience"))==true)
//			restrictions.add(new IsComplexityExperience());
//		if(additionalParameters.get("maxWorkload")!=null)
//			restrictions.add(new MaxWorkload());
//
//		//general restrictions (synergy)
//		if(additionalParameters.get("existCerebro")!=null && ((Boolean)additionalParameters.get("existCerebro"))==true)
//			restrictions.add(new ExistCerebro());
//		if(additionalParameters.get("allBelbinCategories")!=null && ((Boolean)additionalParameters.get("allBelbinCategories"))==true)
//			restrictions.add(new AllBelbinCategories());
//		if(additionalParameters.get("allBelbinTypes")!=null && ((Boolean)additionalParameters.get("allBelbinTypes"))==true)
//			restrictions.add(new AllBelbinRoles());
//		if(additionalParameters.get("balanceCategoriesBelbin")!=null && ((Boolean)additionalParameters.get("balanceCategoriesBelbin"))==true)
//			restrictions.add(new isBalanced());
//
//		if(additionalParameters.get("incompWorkersNone")!=null && ((Boolean)additionalParameters.get("incompWorkersNone"))==true)
//			restrictions.add(new IncompatibleWorkers());
//		if(additionalParameters.get("incompWorkers")!=null)
//			restrictions.add(new IncompatibleSelectedWorkers());
//		return restrictions;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleGetSearchArea() throws Exception {
//		StaticData.data.clear();
//		Collection workers = getWorkerDao().loadAll();
//		StaticData.data = new LinkedList();
//		for(Iterator iter = workers.iterator(); iter.hasNext();)
//		{
//			Worker worker = (Worker)iter.next();
//			if(worker.getStatus().equals(WorkerStatus.ACTIVE))
//				StaticData.data.add(worker);
//		}
//		return StaticData.data;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Map handleUpdateMapOtherParam(Map otherParameters) throws Exception {
//
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		otherParameters.put("project", project);
//		Long minTechLevel = ServiceLocator.instance().getTechnicalCompetitionService().getMinTechLevel();
//		Long maxTechLevel = ServiceLocator.instance().getTechnicalCompetitionService().getMaxTechLevel();
//		otherParameters.put("minTechLevel", minTechLevel);
//		otherParameters.put("maxTechLevel", maxTechLevel);
//		otherParameters.put("allWorkerConflicts", getWorkerConflictDao().loadAll());
//		otherParameters.put("rolesBelbin", getRoleBelbinDao().loadAll());
//		otherParameters.put("maxWorkload", otherParameters.get("maxWorkload"));
//		return otherParameters;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Object handleGetSolutionAssigned(Long idProject)
//	{
//		Project project = getProjectDao().load(idProject);
//		RoleWorker roleWorker = null;
//
//		Collection pRoles = project.getProjectRoles();
//		LinkedList addedRoles = new LinkedList();
//		int i;
//		boolean foundRole = false;
//
//		State actualSolution = new State();
//
//		i=0;
//		while(i<pRoles.size())
//		{
//			LinkedList assignedWorker = new LinkedList();
//			roleWorker = new RoleWorker(((ProjectRoles)pRoles.toArray(new ProjectRoles[pRoles.size()])[i]).getRole(),assignedWorker,((ProjectRoles)pRoles.toArray(new ProjectRoles[pRoles.size()])[i]).getAmountWorkersRole());
//			actualSolution.getCode().add(i, roleWorker);
//			i++;
//		}
//
//
//		for(Iterator iter = project.getCycles().iterator(); iter.hasNext();)
//		{
//			Cycle cycle = (Cycle)iter.next();
//			for(Iterator iRoles = cycle.getAssignedRoles().iterator(); iRoles.hasNext();)
//			{
//				AssignedRole assignedRole = (AssignedRole)iRoles.next();
//
//				if(!addedRoles.contains(assignedRole) && assignedRole.getStatus().equals(WorkerRoleStatus.ACTIVE))
//				{
//					addedRoles.add(assignedRole);
//					i=0;
//					foundRole = false;
//					while(i<actualSolution.getCode().size() && !foundRole)
//					{
//						if(((RoleWorker)actualSolution.getCode().get(i)).getRole().equals(assignedRole.getRoles()))
//						{
//							roleWorker = (RoleWorker)actualSolution.getCode().get(i);
//							foundRole = true;
//						}
//						else
//							i++;
//					}
//
//					if(foundRole)
//					{
//						LinkedList assignedWorkers = roleWorker.getWorkers();
//						assignedWorkers.add(assignedRole.getWorkers());
//						roleWorker.setWorkers(assignedWorkers);
//						roleWorker.setNeededWorkers(roleWorker.getNeededWorkers()-1);
//						roleWorker.getFixedWorkers().add(assignedRole.getWorkers());
//					}
//				}
//			}
//		}
//
//
//		RoleWorkerVO rwVO = null;
//		SolutionVO solVO = new SolutionVO();
//		solVO.setCost(new Float("0.0000"));
//		for (int j = 0; j < actualSolution.getCode().size(); j++) {
//			RoleWorker rw = (RoleWorker) actualSolution.getCode().get(j);
//			rwVO = new RoleWorkerVO();
//			rwVO.setRoleVO(getRoleDao().toRoleVO((Role)rw.getRole()));
//			LinkedList listWorkers = rw.getWorkers();
//			getWorkerDao().toWorkerVOCollection(listWorkers);
//			rwVO.setWorkersVO(listWorkers);
//			solVO.add(rwVO);
//		}
//
//		return solVO;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Object handleGetInitialSolution(Long[] idRoles,Long[] cantNeededWorkers, String[] idWorkers, Long idProject)
//	throws Exception {
//		Project project = getProjectDao().load(idProject); //proyecto actual 
//		RoleWorker roleWorker = null;
//
//		Collection pRoles = project.getProjectRoles(); //roles necesarios projecto
//		LinkedList addedRoles = new LinkedList();
//		int i;
//		boolean foundRole = false;
//
//		RoleWorkerState actualSolution = new RoleWorkerState();
//
//		i=0;
//		//create role workers for solution from project roles selected
//		while(i<pRoles.size())
//		{
//			LinkedList assignedWorker = new LinkedList();
//			roleWorker = new RoleWorker(((ProjectRoles)pRoles.toArray(new ProjectRoles[pRoles.size()])[i]).getRole(), assignedWorker, ((ProjectRoles)pRoles.toArray(new ProjectRoles[pRoles.size()])[i]).getAmountWorkersRole());
//			actualSolution.getCode().add(actualSolution.getCode().size(), roleWorker);
//			i++;
//		}
//
//		//fill role workers according to assigned roles
//		for(Iterator iter = project.getCycles().iterator(); iter.hasNext();)
//		{
//			Cycle cycle = (Cycle)iter.next();
//			for(Iterator iRoles = cycle.getAssignedRoles().iterator(); iRoles.hasNext();)
//			{
//				AssignedRole assignedRole = (AssignedRole)iRoles.next();
//
//				if(!addedRoles.contains(assignedRole) && assignedRole.getStatus().equals(WorkerRoleStatus.ACTIVE))
//				{
//					addedRoles.add(assignedRole);
//					i=0;
//					foundRole = false;
//					while(i<actualSolution.getCode().size() && !foundRole)
//					{
//						if(((RoleWorker)actualSolution.getCode().get(i)).getRole().equals(assignedRole.getRoles()))
//						{
//							roleWorker = (RoleWorker)actualSolution.getCode().get(i);
//							foundRole = true;
//						}
//						else
//							i++;
//					}
//
//					if(foundRole)
//					{
//						LinkedList assignedWorkers = (LinkedList) roleWorker.getWorkers();
//						assignedWorkers.add(assignedRole.getWorkers());
//						roleWorker.setWorkers(assignedWorkers);
//						roleWorker.setNeededWorkers(roleWorker.getNeededWorkers()-1);
//						roleWorker.getFixedWorkers().add(assignedRole.getWorkers());
//					}
//				}
//			}
//		}
//
//		int j=0;
//		String[] workers;
//		Worker worker = null;
//		for(i=0; i<idRoles.length; i++)
//		{
//			if(idRoles[i]!=null)
//			{
//				j=0;
//				foundRole = false;
//				while(j<actualSolution.getCode().size() && !foundRole)
//				{
//					Long idRole = ((Role)((RoleWorker)actualSolution.getCode().get(j)).getRole()).getId();
//					if(idRole.equals(idRoles[i]))
//					{
//						roleWorker = (RoleWorker)actualSolution.getCode().get(i);
//						foundRole = true;
//					}
//					else
//						j++;
//				}
//
//				if(foundRole)
//				{
//					LinkedList workerEntities = new LinkedList();
//					if(idWorkers[i]!=null && !idWorkers[i].equals(""))
//					{
//						workers = idWorkers[i].split(",");
//						for(j=0; j<workers.length; j++)
//						{
//							worker = getWorkerDao().load(new Long(workers[j]));
//							if(!roleWorker.getWorkers().contains(worker))
//							{
//								workerEntities.add(worker);
//							}
//						}
//
//						while(workerEntities.size()>cantNeededWorkers[i])
//							workerEntities.removeLast();
//
//						roleWorker.getWorkers().addAll(workerEntities);
//						roleWorker.getFixedWorkers().addAll(workerEntities);
//						roleWorker.setNeededWorkers(roleWorker.getNeededWorkers()-workerEntities.size());
//					}
//				}
//			}
//		}
//		return actualSolution;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleGetProspectiveWorkers(Long currentRole, Long[] idRoles,
//			String[] idWorkers, Long[] cantNeededWorkers, Map otherParameters)
//	throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//
//		Project project = getProjectDao().load(idProject);
//		otherParameters = updateMapOtherParam(otherParameters);
//		Collection workers = getSearchArea(); //trabajadores que se pueden asignar a la soluciona (pertenecientes al grupo seleccionado en la pantalla y que tienen estado ACTIVE)
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters)); //funciones objetivo seleccionadas en la UI
//		LinkedList restrictions = new LinkedList();  //en lista de restricciones poner marcadas en la UI
//		restrictions.addAll(getRestrictions(otherParameters));
//		RoleWorkerState state = new RoleWorkerState();
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		//Ejemplo librer�a
//		//Definiendo el operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		//Inicializaci�n y Asignaci�n de las clases del problema
//		ProblemX problem = new ProblemX(idRoles, cantNeededWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(state);
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod")); // factores ponderados para Jefe de PRoyecto
//		problem.setTypeProblem(ProblemType.Maximizar);//fijo
//		problem.setFunction(objectiveFunctions); //lista definida anteriormente
//		operator.setProblem(problem); // no tocar
//		Solution solution = new Solution(restrictions,problem,workers); // igual
//
//		Strategy.getStrategy().setProblem(problem);
//
//
//		boolean foundWorker = false;
//		int n = 0;
//		int pos = -1;
//		while(n < idRoles.length && !foundWorker)
//		{
//			if(currentRole.equals(idRoles[n]))
//			{
//				foundWorker = true;
//				pos = n;
//			}
//			n++;
//		}
//
//		String[] currentWorkers = {};
//		if(pos!=-1 && idWorkers[pos]!=null)
//			currentWorkers = idWorkers[pos].split(",");
//
//		State sol = (State)getInitialSolution(idRoles, cantNeededWorkers, idWorkers, idProject);
//
//		LinkedList rwTempList = new LinkedList();
//		RoleWorker rw = null;
//
//		boolean roleExist = false;
//		//Recorriendo los RoleWorker de la solución
//		for (int i = 0; i < sol.getCode().size(); i++) {
//			RoleWorker roleWorker = (RoleWorker) sol.getCode().get(i);
//			if(roleWorker.getRole().equals(getRoleDao().load(currentRole)))
//			{
//				rw = roleWorker;
//				roleExist = true;
//
//			}
//		}
//
//		if(!roleExist)
//		{
//			rw = new RoleWorker(getRoleDao().load(currentRole), rwTempList, 0L);
//			sol.getCode().add(sol.getCode().size(), rw);
//
//		}
//
//		LinkedList rclTemp = new LinkedList();
//		double costoMiope = 0f;
//
//                
//                //Continuar aqui recorrer trabajadores disponibles en el search area()
//		for(Iterator iter = workers.iterator(); iter.hasNext();) // para cada trabajador del searcharea
//		{
//			Worker w = (Worker)iter.next();
//			if(!rw.getWorkers().contains(w))
//			{
//				rw.getWorkers().add(w);
//				boolean checkedRestrictions; 
//
//				int count = 0;
//				for (int i = 0; i < sol.getCode().size(); i++) {
//					RoleWorker roleWorker = (RoleWorker) sol.getCode().get(i);
//					if(roleWorker.getNeededWorkers()>0)
//						count++;
//				}
//
//				if(count > 1){
//
//					checkedRestrictions = solution.checkIndividualRestrictions(sol);//((Solution)problem.getCodification()).checkIndividualRestrictions(sol);
//				}
//				else{
//					checkedRestrictions = solution.checkIndividualRestrictions(sol); //usar validate state
//					if(checkedRestrictions){
//						checkedRestrictions = solution.checkTeamRestrictions(sol);
//					}
//				}
//				if(checkedRestrictions) //si cumple las restricciones
//				{
//					problem.Evaluate(sol); // evaluo el state
//					DecimalFormat df = new DecimalFormat("0.000#");
//					String evalStr = df.format(sol.getEvaluation().get(sol.getEvaluation().size()-1));
//					String costStr = evalStr+"/1";
//
//					WorkerVO wVO = getWorkerDao().toWorkerVO(w);
//					wVO.setAddress(costStr);
//					wVO.setFax(currentRole.toString());
//
//					//List<Double> eval = sol.getEvaluation();
//					//int numEval = 0;
//					/*for(Iterator iter1 = objectiveFunctions.iterator(); iter1.hasNext();)					{
//						ObjetiveFunction objfunction = (ObjetiveFunction)iter1.next();
//						if(objfunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MaximizeCompetency")){
//							wVO.setMaxComp((Float.valueOf(df.format(eval.get(numEval)))));
//						}
//						if(objfunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeCostDistance")){
//							wVO.setMinCost((Float.valueOf(df.format(eval.get(numEval)))));
//						}
//						if(objfunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeIncompatibilities")){
//							wVO.setMinIncomp((Float.valueOf(df.format(eval.get(numEval)))));
//						}
//						if(objfunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeWorkload")){
//							wVO.setMinWorkLoad((Float.valueOf(df.format(eval.get(numEval)))));
//						}
//						numEval++;
//					}*/
//					boolean found = false;
//					for(int j=0; j<currentWorkers.length && !found; j++)
//					{
//						if(!currentWorkers[j].equalsIgnoreCase("") && wVO.getId().equals(Long.parseLong(currentWorkers[j])))
//						{
//							wVO.setPhone("checked");
//							found = true;
//						}
//					}
//
//					if(rw.getFixedWorkers().contains(w))
//						wVO.setIdCard("FIXED");
//
//					rclTemp.add(wVO);
//				}
//				rw.getWorkers().remove(w);
//			}
//			else
//			{
//				problem.Evaluate(sol);
//
//				DecimalFormat df = new DecimalFormat("0.000#");
//				String evalStr = df.format(costoMiope);
//
//				String costStr = evalStr+"/1";
//
//				WorkerVO wVO = getWorkerDao().toWorkerVO(w);
//				wVO.setAddress(costStr);
//				wVO.setFax(currentRole.toString());
//
//				problem.Evaluate(sol);
//				List<Double> eval = sol.getEvaluation();
//				int numEval = 0;
//				for(Iterator iter1 = objectiveFunctions.iterator(); iter1.hasNext();)					{
//					ObjetiveFunction objfunction = (ObjetiveFunction)iter1.next();
//					if(objfunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MaximizeCompetency")){
//						wVO.setMaxComp((Float.valueOf(df.format(eval.get(numEval)))));
//					}
//					if(objfunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeCostDistance")){
//						wVO.setMinCost((Float.valueOf(df.format(eval.get(numEval)))));
//					}
//					if(objfunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeIncompatibilities")){
//						wVO.setMinIncomp((Float.valueOf(df.format(eval.get(numEval)))));
//					}
//					if(objfunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeWorkload")){
//						wVO.setMinWorkLoad((Float.valueOf(df.format(eval.get(numEval)))));
//					}
//					numEval++;
//				}
//
//				boolean found = false;
//				for(int j=0; j<currentWorkers.length && !found; j++)
//				{
//					if(!currentWorkers[j].equalsIgnoreCase("") && wVO.getId().equals(Long.parseLong(currentWorkers[j])))
//					{
//						wVO.setPhone("checked");
//						found = true;
//					}
//				}
//
//				if(rw.getFixedWorkers().contains(w))
//					wVO.setIdCard("FIXED");
//
//				rclTemp.add(wVO);
//
//			}
//		}
//
//		//GuardarComportamientoRestricciones("Metodo1");
//		//ordenar rcltemp
//		WorkerVO[] tempWorkers = (WorkerVO[])rclTemp.toArray(new WorkerVO[rclTemp.size()]);
//		LinkedList toReturn = new LinkedList();
//		int nPos = 0;
//		boolean inserted = false;
//		for(int i=0; i<tempWorkers.length; i++)
//		{
//			Float value = Float.parseFloat(tempWorkers[i].getAddress().split("/")[0]);
//			nPos = 0;
//			inserted = false;
//			while(nPos<toReturn.size() && !inserted)
//			{
//				if(Float.parseFloat(((WorkerVO)toReturn.get(nPos)).getAddress().split("/")[0])<value)
//				{
//					toReturn.add(nPos, tempWorkers[i]);
//					inserted = true;
//				}
//				nPos++;
//			}
//			if(!inserted)
//				toReturn.addLast(tempWorkers[i]);
//		}
//		return toReturn;
//	}
//
//	@SuppressWarnings({ "unchecked", "static-access" })
//	@Override
//	protected Collection handleHC(Long[] idRoles,
//		Long[] cantWorkers, Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		//Creando el fichero para guardar los resultados de cada iteraci�n
//		String fileName = "D:/Resultados/HC.txt";
//		PrintWriter salida = CreateFile(idRoles, cantWorkers, fileName,objectiveFunctions, (TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//		//state.add(actualSolution);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		String a = (String)params.get("tecnica");
//		Strategy.setStrategyVariable(a);
//		//Operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod(TypeSolutionMethod.FactoresPonderados);
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setIdProject(idProject);
//		operator.setInitialGenerate(0);
//		operator.setIdRoles(idRoles);
//		RoleWorkerState saGeneratedSol;
//		if (Strategy.StrategyVariable.equals("preservacion")){
//		lista = saConstruction.getFilteredWorkers();
//		 saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolutionModif();
//		}
//		else 
//		{saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();}
//		problem.setState(saGeneratedSol);
//		LinkedList<SolutionVO> solVOL = null;
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//
//				// Activar Generador
//				Strategy.getStrategy().calculateTime = true;
//				Strategy.getStrategy().saveListBestStates = false;
//				Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(30000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.HillClimbing);
//
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(Strategy.getStrategy().getBestState() != null){
//					solVOL = writeSolutions(salida, Strategy.getStrategy().listBest, Strategy.getStrategy().listRefPoblacFinal, problem, otherParameters, objectiveFunctions, Strategy.getStrategy().getBestState(), Strategy.getStrategy().timeExecute, "HillClimbing");
//				}
//				else{
//					return null;
//				}
//				
//				GuardarComportamientoRestricciones(Strategy.generator.getType().name().toString());
//				
//				Strategy.destroyExecute();
//			}	
//			return solVOL;
//		}
//		else
//			return null;
//	}
//
//	@SuppressWarnings({ "unchecked"})
//	private LinkedList<SolutionVO> writeSolutions(PrintWriter salida, List<State> listStates, List<State> paretoSolutions, ProblemX problem, Map otherParameters, ArrayList<ObjetiveFunction> objectiveFunctions, State bestSolution, long time, String algorithm_name) throws Exception{
//		LinkedList<SolutionVO> solVOL = new LinkedList<SolutionVO>();
//
//		//Creando para guardar los tiempos
//		FileWriter fw = null;
//		File file = new File("D:/Resultados/Timede-" + algorithm_name + ".txt");		
//		BufferedWriter bw = null;		
//		PrintWriter salidaTiempo = null;
//		if(!file.exists())
//		{
//			try {
//				fw = new FileWriter("D:/Resultados/Timede-" + algorithm_name + ".txt");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			bw = new BufferedWriter(fw);
//			salidaTiempo = new PrintWriter(bw);
//		}
//		else
//		{
//			try {
//				fw = new FileWriter("D:/Resultados/Timede-" + algorithm_name + ".txt", true);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			bw = new BufferedWriter(fw);
//			salidaTiempo = new PrintWriter(bw);
//		}
//		salidaTiempo.print(time);
//		salidaTiempo.print("	");
//		salidaTiempo.close();
//
//		//Escribiendo todas las soluciones encontradas en el txt
//	/*	DecimalFormat df = new DecimalFormat("0.000#");
//		for(Iterator it = listStates.iterator(); it.hasNext();)
//		{
//			State sol = (State)it.next();
//			for (int j = 0; j < sol.getCode().size(); j++) {
//				RoleWorker s = (RoleWorker)sol.getCode().get(j);
//
//				for(Iterator wkr = s.getWorkers().iterator(); wkr.hasNext();)
//				{
//					Worker wk = (Worker)wkr.next();
//					salida.print(wk.getWorkerName());
//					salida.print("	");
//				}
//			}
//			List<Double> evaluations = sol.getEvaluation();
//			for (int k = 0; k < evaluations.size(); k++) {
//				Double eval = evaluations.get(k);
//				if(k==0 || k== 3){
//					eval= eval-0.01;
//				}
//				salida.print(eval);
//				salida.print("	");
//			}
//			salida.println("Solution");
//		}
//		
//		//Creando para guardar los elementos del frente de Pareto
//		if(paretoSolutions.size()>0){
//			fw = null;
//			file = new File("D:/Resultados/ParetoSolutions-" + algorithm_name + ".txt");		
//			bw = null;		
//			PrintWriter salidaXa = null;
//			if(!file.exists())
//			{
//				try {
//					fw = new FileWriter("D:/Resultados/ParetoSolutions-" + algorithm_name + ".txt");
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				bw = new BufferedWriter(fw);
//				salidaXa = new PrintWriter(bw);
//			}
//			else
//			{
//				try {
//					fw = new FileWriter("D:/Resultados/ParetoSolutions-" + algorithm_name + ".txt", true);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//				bw = new BufferedWriter(fw);
//				salidaXa = new PrintWriter(bw);
//			}
//			
//			//Escribiendo las soluciones del frente de Pareto en txt
//			for(Iterator it = paretoSolutions.iterator(); it.hasNext();)
//			{
//				State sol = (State)it.next();
//				for (int j = 0; j < sol.getCode().size(); j++) {
//					RoleWorker s = (RoleWorker)sol.getCode().get(j);
//
//					for(Iterator wkr = s.getWorkers().iterator(); wkr.hasNext();)
//					{
//						Worker wk = (Worker)wkr.next();
//						salidaXa.print(wk.getWorkerName());
//						salidaXa.print("	");
//					}
//				}
//				List<Double> evaluations = sol.getEvaluation();
//				for (int k = 0; k < evaluations.size(); k++) {
//					Double eval = evaluations.get(k);
//					if(k==0 || k== 3){
//						eval= eval-0.01;
//					}
//					salidaXa.print(eval);
//					salidaXa.print("	");
//				}
//				salidaXa.println("ParetoSolution");
//			}
//			salidaXa.close();
//		}
//		*/
//		//Escribiendo la mejor soluci�n encontrada en el txt
//		for (int j = 0; j < bestSolution.getCode().size(); j++) {
//			RoleWorker s = (RoleWorker)bestSolution.getCode().get(j);
//			for(Iterator wkr = s.getWorkers().iterator(); wkr.hasNext();)
//			{
//				Worker wk = (Worker)wkr.next();
//				salida.print(wk.getWorkerName());
//				salida.print("	");
//			}
//		}
//		List<Double> evaluations = bestSolution.getEvaluation();
//		for (int k = 0; k < evaluations.size(); k++) {
//			Double eval = evaluations.get(k);
//		/*	if(k==0 || k== 3){
//				eval= eval-0.01;
//			}*/
//			salida.print(eval);
//			salida.print("	");
//		}
//		salida.println("BestSolution");
//		salida.close();
//
//		//Mostrando la mejor soluci�n econtrada en pantalla
//		DecimalFormat df = new DecimalFormat("0.000#");
//		RoleWorkerVO rwVO = null;
//		SolutionVO solVO = new SolutionVO();
//		solVO = new SolutionVO();
//		String evalStr ;
//		evalStr= df.format(bestSolution.getEvaluation().get(bestSolution.getEvaluation().size()-1));
//
//		solVO.setCost(new Float(evalStr));
//		solVO.setMaxComp(-1);
//		solVO.setMinCost(-1);
//		solVO.setMinIncomp(-1);
//		solVO.setMinWorkLoad(-1);
//		for (int i = 0; i < bestSolution.getCode().size(); i++) {
//			RoleWorker rw = (RoleWorker) bestSolution.getCode().get(i);
//			rwVO = new RoleWorkerVO();
//			rwVO.setRoleVO(getRoleDao().toRoleVO((Role)rw.getRole()));
//			LinkedList listWorkers = rw.getWorkers();
//
//			String costoMiope = "";
//
//			for(Iterator iWork=listWorkers.iterator(); iWork.hasNext();)
//			{
//				Worker element = (Worker)iWork.next();
//				String evalStr1 = costoMiope;
//				Collection sols = new LinkedList<Solution>();
//				//Convertir el Map en una Lista de RoleWorker
//				LinkedList<RoleWorker> solut1 = new LinkedList<RoleWorker>();
//				for (int k = 0; k < bestSolution.getCode().size(); k++) {
//					RoleWorker rw1 = (RoleWorker) bestSolution.getCode().get(k);
//					solut1.add(rw1);
//				}
//				sols.add(solut1);
//				//Recorriendo las funciones objetivo para mostrar el valor de cada uno de ellas en la evaluaci�n
//				Float[] eval = new Float[4];
//				eval[0] = -1f;
//				eval[1] = -1f;
//				eval[2] = -1f;
//				eval[3] = -1f;
//				org.andromda.teamsoft.vo.Solution sol = new org.andromda.teamsoft.vo.Solution(null, sols, Float.valueOf(df.format(eval[0])), Float.valueOf(df.format(eval[1])), Float.valueOf(df.format(eval[2])), Float.valueOf(df.format(eval[3])), Float.valueOf(evalStr));
//				org.andromda.teamsoft.vo.RoleWorker rw1 = new org.andromda.teamsoft.vo.RoleWorker(null, ((Role)rw.getRole()).getId(), rw.getWorkers(), rw.getFixedWorkers(), rw.getNeededWorkers());
//				LinkedList<WorkerSolutionVO> list = (LinkedList<WorkerSolutionVO>) handleWorkerToWorkerSolutionVO(otherParameters, problem.getProject().getId(), sol, evalStr1, rw1, element.getId());
//				WorkerSolutionVO workerSolutionVO = list.toArray(new WorkerSolutionVO[list.size()])[0];
//				rwVO.getWorkersVO().add(workerSolutionVO);
//			}
//			solVO.add(rwVO);
//		}
//		solVOL.add(solVO);
//		return solVOL;
//
//	}
//	
//
//	private LinkedList<SolutionVO> writeSolutionsMulti(PrintWriter salida, List<State> allSolutions, List<State> bestSolutions, ProblemX problem, Map otherParameters, ArrayList<ObjetiveFunction> objectiveFunctions, String time, String algorithm_name) throws Exception{
//		LinkedList<SolutionVO> solVOL = new LinkedList<SolutionVO>();
//
//		//Creando para guardar los tiempos
//		FileWriter fw = null;
//		File file = new File("D:/Resultados/Time-" + algorithm_name + ".txt");		
//		BufferedWriter bw = null;		
//		PrintWriter salidaTiempo = null;
//		if(!file.exists())
//		{
//			try {
//				fw = new FileWriter("D:/Resultados/Time-" + algorithm_name + ".txt");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			bw = new BufferedWriter(fw);
//			salidaTiempo = new PrintWriter(bw);
//		}
//		else
//		{
//			try {
//				fw = new FileWriter("D:/Resultados/Time-" + algorithm_name + ".txt", true);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			bw = new BufferedWriter(fw);
//			salidaTiempo = new PrintWriter(bw);
//		}
//		salidaTiempo.print(otherParameters.get("iteration"));
//		salidaTiempo.print(";");
//		salidaTiempo.print(time);
//		salidaTiempo.println();
//		salidaTiempo.close();
//
//		//Creando para guardar los mejores valores
//		fw = null;
//		file = new File("D:/Resultados/ParetoSolutions-" + algorithm_name + ".txt");		
//		bw = null;		
//		PrintWriter salidaXa = null;
//		if(!file.exists())
//		{
//			try {
//				fw = new FileWriter("D:/Resultados/ParetoSolutions-" + algorithm_name + ".txt");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			bw = new BufferedWriter(fw);
//			salidaXa = new PrintWriter(bw);
//			salidaXa.print("Proyecto;Ejecucion;Estado;Cantidad de FO;Evaluaciones en FO");
//			salidaXa.println();
//		}
//		else
//		{
//			try {
//				fw = new FileWriter("D:/Resultados/ParetoSolutions-" + algorithm_name + ".txt", true);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			bw = new BufferedWriter(fw);
//			salidaXa = new PrintWriter(bw);
//		}
//		//Escribiendo las soluciones encontradas
//		
//		/*int iterac = 0;
//		for(Iterator it = allSolutions.iterator(); it.hasNext();)
//		{
//			salida.print(otherParameters.get("iteration"));
//			salida.print(";");
//			salida.print(iterac);
//			salida.print(";");
//			salida.print("[");
//			State solActual = (State)it.next();
//			for (int j = 0; j < solActual.getCode().size(); j++) {
//				RoleWorker s1 = (RoleWorker)solActual.getCode().get(j);
//				for(Iterator wkr = s1.getWorkers().iterator(); wkr.hasNext();)
//				{
//					Worker wk = (Worker)wkr.next();
//					salida.print(wk.getWorkerName());
//					if(j < solActual.getCode().size()-1){
//						salida.print(",");
//					}
//				}
//
//			}
//			salida.print("];");
//			salida.print(objectiveFunctions.size());
//			salida.print(";");
//			salida.print("[");
//			List<Double> evaluationsXa = solActual.getEvaluation();
//			for (int k = 0; k < evaluationsXa.size(); k++) {
//				Double eval = evaluationsXa.get(k);
//				
//				salida.print(eval);
//				if(k < evaluationsXa.size()-1){
//					salida.print(",");
//				}
//
//			}
//			salida.print("]");
//			salida.println();
//			iterac++;
//		}
//		
//		salida.close();*/
//		
//		//Escribiendo las mejores soluciones encontradas (una en el monoobjetivo o el frente de Pareto en el multiobjetivo) en el txt
//		for(Iterator it = bestSolutions.iterator(); it.hasNext();)
//		{
//			State solActual = (State)it.next();
//		
//			Long idProject = (Long) otherParameters.get("idProject");
//			ProjectVO projectVO = ServiceLocator.instance().getProjectService().getProject(idProject);
//			salidaXa.print(projectVO.getProjectName());
//			salidaXa.print(";");
//			salidaXa.print(otherParameters.get("iteration"));
//			salidaXa.print(";");
//			salidaXa.print("[");
//			
//			for (int j = 0; j < solActual.getCode().size(); j++) {
//				RoleWorker s1 = (RoleWorker)solActual.getCode().get(j);
//				for(Iterator wkr = s1.getWorkers().iterator(); wkr.hasNext();)
//				{
//					Worker wk = (Worker)wkr.next();
//					salidaXa.print(wk.getWorkerName());
//					if(j < solActual.getCode().size()-1){
//						salidaXa.print(",");
//					}
//				}
//
//			}
//			salidaXa.print("];");
//			salidaXa.print(objectiveFunctions.size());
//			salidaXa.print(";");
//			salidaXa.print("[");
//			List<Double> evaluationsXa = solActual.getEvaluation();
//			for (int k = 0; k < evaluationsXa.size(); k++) {
//				Double eval = evaluationsXa.get(k);
//				
//				salidaXa.print(eval);
//				if(k < evaluationsXa.size()-1){
//					salidaXa.print(",");
//				}
//
//			}
//			salidaXa.print("]");
//			salidaXa.println();
//		}
//
//		//salidaXa.print("Tiempo (ms):");
//		//salidaXa.println(time);
//		//salida.close();
//		salidaXa.close();
//		
//
//		/*DecimalFormat df = new DecimalFormat("0.000#");
//		//Mostrando las mejores soluciones econtradas en pantalla
//		for (Iterator iterator = bestSolutions.iterator(); iterator.hasNext();) {
//			State state = (State) iterator.next();
//
//			RoleWorkerVO rwVO = null;
//			SolutionVO solVO = new SolutionVO();
//			solVO = new SolutionVO();
//			String evalStr = "-1";
//			if(problem.getTypeSolutionMethod().equals(TypeSolutionMethod.FactoresPonderados)){
//				evalStr = df.format(state.getEvaluation().get(state.getEvaluation().size()-1));
//			}
//
//			solVO.setCost(new Float(evalStr));
//			int j = 0;
//			solVO.setMaxComp(-1);
//			solVO.setMinCost(-1);
//			solVO.setMinIncomp(-1);
//			solVO.setMinWorkLoad(-1);
//			for (Iterator iter = objectiveFunctions.iterator(); iter.hasNext();) {
//				ObjetiveFunction objFunction = (ObjetiveFunction) iter.next();
//				if(objFunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MaximizeCompetency")){
//					solVO.setMaxComp(Float.valueOf(df.format(state.getEvaluation().get(j))));
//				}
//				if(objFunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeWorkload")){
//					solVO.setMinWorkLoad(Float.valueOf(df.format(Float.valueOf(df.format(state.getEvaluation().get(j))))));
//				}
//				if(objFunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeIncompatibilities")){
//					solVO.setMinIncomp(Float.valueOf(df.format(Float.valueOf(df.format(state.getEvaluation().get(j))))));
//				}
//				if(objFunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeCostDistance")){
//					solVO.setMinCost(Float.valueOf(df.format(Float.valueOf(df.format(state.getEvaluation().get(j))))));
//				}
//				j++;	
//			}
//
//
//			for (int i = 0; i < state.getCode().size(); i++) {
//				RoleWorker rw = (RoleWorker) state.getCode().get(i);
//				rwVO = new RoleWorkerVO();
//				rwVO.setRoleVO(getRoleDao().toRoleVO((Role)rw.getRole()));
//				LinkedList listWorkers = rw.getWorkers();
//
//				String costoMiope = "";
//
//				for(Iterator iWork=listWorkers.iterator(); iWork.hasNext();)
//				{
//					Worker element = (Worker)iWork.next();
//					String evalStr1 = costoMiope;
//					Collection sols = new LinkedList<Solution>();
//					//Convertir el Map en una Lista de RoleWorker
//					LinkedList<RoleWorker> solut1 = new LinkedList<RoleWorker>();
//					for (int k = 0; k < state.getCode().size(); k++) {
//						RoleWorker rw1 = (RoleWorker) state.getCode().get(k);
//						solut1.add(rw1);
//					}
//					sols.add(solut1);
//					//Recorriendo las funciones objetivo para mostrar el valor de cada uno de ellas en la evaluaci�n
//					Float[] eval = new Float[4];
//					eval[0] = -1f;
//					eval[1] = -1f;
//					eval[2] = -1f;
//					eval[3] = -1f;
//					int l = 0;
//					for (Iterator iter = objectiveFunctions.iterator(); iter.hasNext();) {
//						ObjetiveFunction objFunction = (ObjetiveFunction) iter.next();
//						if(objFunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MaximizeCompetency")){
//							eval[0] = Float.valueOf(state.getEvaluation().get(l).toString());//Float.valueOf(e.toString());
//						}
//						if(objFunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeWorkload")){
//							eval[1] = Float.valueOf(state.getEvaluation().get(l).toString());
//						}
//						if(objFunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeIncompatibilities")){
//							eval[2] = Float.valueOf(state.getEvaluation().get(l).toString());
//						}
//						if(objFunction.getClass().getName().equals("org.metaheuristics.objectiveFunctions.MinimizeCostDistance")){
//
//							eval[3] = Float.valueOf(state.getEvaluation().get(l).toString());						
//						}
//						l++;
//					}
//					if(problem.getTypeSolutionMethod()==TypeSolutionMethod.FactoresPonderados){//Factores ponderados
//						org.andromda.teamsoft.vo.Solution sol = new org.andromda.teamsoft.vo.Solution(null, sols, Float.valueOf(df.format(eval[0])), Float.valueOf(df.format(eval[1])), Float.valueOf(df.format(eval[2])), Float.valueOf(df.format(eval[3])), Float.valueOf(evalStr));
//						org.andromda.teamsoft.vo.RoleWorker rw1 = new org.andromda.teamsoft.vo.RoleWorker(null, ((Role)rw.getRole()).getId(), rw.getWorkers(), rw.getFixedWorkers(), rw.getNeededWorkers());
//						LinkedList<WorkerSolutionVO> list = (LinkedList<WorkerSolutionVO>) handleWorkerToWorkerSolutionVO(otherParameters, problem.getProject().getId(), sol, evalStr1, rw1, element.getId());
//						WorkerSolutionVO workerSolutionVO = list.toArray(new WorkerSolutionVO[list.size()])[0];
//						rwVO.getWorkersVO().add(workerSolutionVO);
//					}
//					else{
//						org.andromda.teamsoft.vo.Solution sol = new org.andromda.teamsoft.vo.Solution(null, sols, Float.valueOf(df.format(eval[0])), Float.valueOf(df.format(eval[1])), Float.valueOf(df.format(eval[2])), Float.valueOf(df.format(eval[3])), -1);
//						org.andromda.teamsoft.vo.RoleWorker rw1 = new org.andromda.teamsoft.vo.RoleWorker(null, ((Role)rw.getRole()).getId(), rw.getWorkers(), rw.getFixedWorkers(), rw.getNeededWorkers());
//						LinkedList<WorkerSolutionVO> list = (LinkedList<WorkerSolutionVO>) handleWorkerToWorkerSolutionVO(otherParameters, problem.getProject().getId(), sol, evalStr1, rw1, element.getId());
//						WorkerSolutionVO workerSolutionVO = list.toArray(new WorkerSolutionVO[list.size()])[0];
//						rwVO.getWorkersVO().add(workerSolutionVO);
//					}
//				}
//				solVO.add(rwVO);
//			}
//			solVOL.add(solVO);
//		}*/
//		return solVOL;
//	}
//
//
//	@Override
//	protected Collection handleRandomSearch(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		return null;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleMCMOSA(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		String fileName = "D:/Resultados/MCMOSA.txt";
//		PrintWriter salida = CreateFile(idRoles, cantWorkers, fileName, objectiveFunctions, (TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//		//state.add(actualSolution);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		String a = (String)params.get("tecnica");
//		Strategy.setStrategyVariable(a);
//		//Operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod(TypeSolutionMethod.MultiObjetivoPuro);
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setIdProject(idProject);
//		operator.setInitialGenerate(0);
//		operator.setIdRoles(idRoles);
//		RoleWorkerState saGeneratedSol;
//		if (Strategy.StrategyVariable.equals("preservacion")){
//		lista = saConstruction.getFilteredWorkers();
//		 saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolutionModif();
//		}
//		else 
//		{saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();}
//		
//		problem.setState(saGeneratedSol);
//		LinkedList<SolutionVO> solVOL = null;
//		List<State> solutionsFrente = new LinkedList<State>();
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//
//				//Contando el tiempo de ejecuci�n
//				long initialTime = System.currentTimeMillis();
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//
//				MultiCaseSimulatedAnnealing.alpha = 0.9;
//				MultiCaseSimulatedAnnealing.tinitial = 20.0;
//				MultiCaseSimulatedAnnealing.tfinal = 0.0;
//				MultiCaseSimulatedAnnealing.countIterationsT = 100;
//				
//				//Strategy.greedyValue = false;
//				List<State> allSolutions = new ArrayList<State>();
//				List<State> solutionsActual = new ArrayList<State>();
//
//				// Activar Generador
//				Strategy.getStrategy().saveListBestStates = false;
//				Strategy.getStrategy().saveListStates = false;
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(30000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.MultiCaseSimulatedAnnealing);
//				allSolutions = Strategy.getStrategy().listStates;
//				solutionsFrente = Strategy.getStrategy().listRefPoblacFinal;
//
//				long finalTime = System.currentTimeMillis();
//				long realTime = finalTime - initialTime;
//				GuardarComportamientoRestricciones(Strategy.generator.getType().name().toString());
//
//				Strategy.destroyExecute();
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(solutionsActual != null)
//					solVOL = writeSolutionsMulti(salida, allSolutions, solutionsFrente, problem, otherParameters, objectiveFunctions, String.valueOf(realTime), "MultiCaseSimulatedAnnealing");
//				else
//					return null;
//			}	
//			return solutionsFrente;
//		}
//		else
//			return null;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleMultiobjectiveHCDistance(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		String fileName = "D:/Resultados/MultiHCDistance.txt";
//		PrintWriter salida = CreateFile(idRoles, cantWorkers, fileName, objectiveFunctions, (TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//		//state.add(actualSolution);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		String a = (String)params.get("tecnica");
//		Strategy.setStrategyVariable(a);
//		//Operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod(TypeSolutionMethod.MultiObjetivoPuro);
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setIdProject(idProject);
//		operator.setInitialGenerate(0);
//		operator.setIdRoles(idRoles);
//		RoleWorkerState saGeneratedSol;
//		if (Strategy.StrategyVariable.equals("preservacion")){
//		lista = saConstruction.getFilteredWorkers();
//		 saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolutionModif();
//		}
//		else 
//		{saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();}
//		problem.setState(saGeneratedSol);
//		LinkedList<SolutionVO> solVOL = null;
//		List<State> solutionsFrente = new LinkedList<State>();
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//
//				//Contando el tiempo de ejecuci�n
//				long initialTime = System.currentTimeMillis();
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//
//				//Strategy.greedyValue = false;
//				List<State> allSolutions = new ArrayList<State>();
//				List<State> solutionsActual = new ArrayList<State>();
//
//				// Activar Generador
//				Strategy.getStrategy().saveListStates = false;
//				MultiobjectiveHillClimbingDistance.sizeNeighbors = 2;
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(30000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.MultiobjectiveHillClimbingDistance);
//				allSolutions = Strategy.getStrategy().listStates;
//				solutionsFrente = Strategy.getStrategy().listRefPoblacFinal;
//
//				long finalTime = System.currentTimeMillis();
//				long realTime = finalTime - initialTime;
//
//				GuardarComportamientoRestricciones(Strategy.generator.getType().name().toString());
//				
//				Strategy.destroyExecute();
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(solutionsActual != null)
//					solVOL = writeSolutionsMulti(salida, allSolutions, solutionsFrente, problem, otherParameters, objectiveFunctions, String.valueOf(realTime), "MultiobjectiveHillClimbingDistance");
//				else
//					return null;
//			}	
//			return solutionsFrente;
//		}
//		else
//			return null;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleMultiobjectiveHCRestart(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		//Creando el fichero para guardar los resultados de cada iteraci�n
//		String fileName = "D:/Resultados/MultiHCRestart.txt";
//		PrintWriter salida = CreateFile(idRoles, cantWorkers, fileName,objectiveFunctions, (TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//		//state.add(actualSolution);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		String a = (String)params.get("tecnica");
//		Strategy.setStrategyVariable(a);
//		//Operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod(TypeSolutionMethod.MultiObjetivoPuro);
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setIdProject(idProject);
//		operator.setInitialGenerate(0);
//		operator.setIdRoles(idRoles);
//		RoleWorkerState saGeneratedSol;
//		if (Strategy.StrategyVariable.equals("preservacion")){
//		lista = saConstruction.getFilteredWorkers();
//		 saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolutionModif();
//		}
//		else 
//		{saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();}
//		problem.setState(saGeneratedSol);
//		LinkedList<SolutionVO> solVOL = null;
//		List<State> solutionsFrente = new LinkedList<State>();
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//
//				//Contando el tiempo de ejecuci�n
//				long initialTime = System.currentTimeMillis();
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//
//				//Strategy.greedyValue = false;
//				List<State> allSolutions = new ArrayList<State>();
//				List<State> solutionsActual = new ArrayList<State>();
//
//				// Activar Generador
//				Strategy.getStrategy().saveListStates = false;
//				MultiobjectiveHillClimbingRestart.sizeNeighbors = 2; 
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(30000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.MultiobjectiveHillClimbingRestart);
//				allSolutions = Strategy.getStrategy().listStates;
//				solutionsFrente = Strategy.getStrategy().listRefPoblacFinal;
//
//				long finalTime = System.currentTimeMillis();
//				long realTime = finalTime - initialTime;
//
//				GuardarComportamientoRestricciones(Strategy.generator.getType().name().toString());
//				
//				Strategy.destroyExecute();
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(solutionsActual != null)
//					solVOL = writeSolutionsMulti(salida, allSolutions, solutionsFrente, problem, otherParameters, objectiveFunctions, String.valueOf(realTime), "MultiobjectiveHillClimbingRestart");
//				else
//					return null;
//			}	
//			return solutionsFrente;
//		}
//		else
//			return null;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleMultiobjectiveSimulatedAnnealing(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		String fileName = "D:/Resultados/UMOSA.txt";
//		PrintWriter salida = CreateFile(idRoles, cantWorkers, fileName, objectiveFunctions, (TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//		//state.add(actualSolution);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		String a = (String)params.get("tecnica");
//		Strategy.setStrategyVariable(a);
//		//Operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod(TypeSolutionMethod.MultiObjetivoPuro);
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setIdProject(idProject);
//		operator.setInitialGenerate(0);
//		operator.setIdRoles(idRoles);
//		RoleWorkerState saGeneratedSol;
//		if (Strategy.StrategyVariable.equals("preservacion")){
//		lista = saConstruction.getFilteredWorkers();
//		 saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolutionModif();
//		}
//		else 
//		{saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();}
//		problem.setState(saGeneratedSol);
//		LinkedList<SolutionVO> solVOL = null;
//		List<State> solutionsFrente = new LinkedList<State>();
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//
//				//Contando el tiempo de ejecuci�n
//				long initialTime = System.currentTimeMillis();
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//				UMOSA.alpha = 0.9;
//				UMOSA.tinitial = 20.0;
//				UMOSA.tfinal = 0.0;
//				UMOSA.countIterationsT = 100;
//				
//				//Strategy.greedyValue = false;
//				List<State> allSolutions = new ArrayList<State>();
//				List<State> solutionsActual = new ArrayList<State>();
//
//				// Activar Generador
//				Strategy.getStrategy().saveListStates = false;
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(30000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.UMOSA);
//				allSolutions = Strategy.getStrategy().listStates;
//				solutionsFrente = Strategy.getStrategy().listRefPoblacFinal;
//
//				long finalTime = System.currentTimeMillis();
//				long realTime = finalTime - initialTime;
//
//				GuardarComportamientoRestricciones(Strategy.generator.getType().name().toString());
//				
//				Strategy.destroyExecute();
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(solutionsActual != null)
//					solVOL = writeSolutionsMulti(salida, allSolutions, solutionsFrente, problem, otherParameters, objectiveFunctions, String.valueOf(realTime), "UMOSA");
//				else
//					return null;
//			}	
//			return solutionsFrente;
//		}
//		else
//			return null;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleMultiobjectiveStochasticHC(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//    	ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		String fileName = "D:/Resultados/MultiHC.txt";
//		PrintWriter salida = CreateFile(idRoles, cantWorkers, fileName,objectiveFunctions,(TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//		//state.add(actualSolution);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		String a = (String)params.get("tecnica");
//		Strategy.setStrategyVariable(a);
//		//Operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod(TypeSolutionMethod.MultiObjetivoPuro);
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setIdProject(idProject);
//		operator.setInitialGenerate(0);
//		operator.setIdRoles(idRoles);
//		RoleWorkerState saGeneratedSol;
//		if (Strategy.StrategyVariable.equals("preservacion")){
//		lista = saConstruction.getFilteredWorkers();
//		 saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolutionModif();
//		}
//		else 
//		{saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();}
//		problem.setState(saGeneratedSol);
//		LinkedList<SolutionVO> solVOL = null;
//		List<State> solutionsFrente = new LinkedList<State>();
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//
//				//Contando el tiempo de ejecuci�n
//				long initialTime = System.currentTimeMillis();
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//
//				//Strategy.greedyValue = false;
//				List<State> allSolutions = new ArrayList<State>();
//				List<State> solutionsActual = new ArrayList<State>();
//
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(30000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.MultiobjectiveStochasticHillClimbing);
//				allSolutions = Strategy.getStrategy().listStates;
//				solutionsFrente = Strategy.getStrategy().listRefPoblacFinal;
//
//				long finalTime = System.currentTimeMillis();
//				long realTime = finalTime - initialTime;
//
//				GuardarComportamientoRestricciones(Strategy.generator.getType().name().toString());
//				
//				Strategy.destroyExecute();
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(solutionsActual != null)
//					solVOL = writeSolutionsMulti(salida, allSolutions, solutionsFrente, problem, otherParameters, objectiveFunctions, String.valueOf(realTime), "MultiobjectiveStochasticHillClimbing");
//				else
//					return null;
//			}	
//			return solutionsFrente;
//		}
//		else
//			return null;
//	}
//
//	private PrintWriter CreateFile(Long[] idRoles, Long[] cantWorkers, String fileName, ArrayList<ObjetiveFunction> functions, TypeSolutionMethod typeSolutionMethod) {
//		FileWriter fw = null;
//		File file = new File(fileName);		
//		BufferedWriter bw = null;	
//		PrintWriter salida = null;
//		if(!file.exists())
//		{
//			try {
//				fw = new FileWriter(fileName);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}// Objeto para que establece origen de los datos
//			bw = new BufferedWriter(fw);// buffer para el manejo de los datos
//			salida = new PrintWriter(bw);
//
//			salida.print("Ejecucion;Estado;Cantidad de FO;Evaluaciones en FO");
//			salida.println();
//			
//			/*for(int i=0; i<idRoles.length; i++)
//			{
//				Role role = getRoleDao().load(idRoles[i]);
//				for(int j=0; j<cantWorkers[i]; j++)
//				{
//					salida.print(role.getRoleName());
//					salida.print("	");
//				}
//			}
//			if(typeSolutionMethod.equals(TypeSolutionMethod.MultiObjetivoPuro))
//			{
//				for (Iterator iterator = functions.iterator(); iterator.hasNext();) 
//				{
//					ObjetiveFunction objetiveFunction = (ObjetiveFunction) iterator
//							.next();
//					char[] name = objetiveFunction.getClass().getName().toCharArray();
//					String function = "";
//					for (int i = 38; i < name.length; i++) 
//					{
//						char c = name[i];
//						function = function + c;
//					}
//					salida.print(function);
//					salida.print("	");
//				}
//			}
//			salida.println("Estado");*/
//		}
//		else
//		{
//			try {
//				fw = new FileWriter(fileName, true);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}// Objeto para que establece origen de los datos
//			bw = new BufferedWriter(fw);// buffer para el manejo de los datos
//			salida = new PrintWriter(bw);
//		}
//
//		/*fw = null;
//		file = new File("D:/SolutionsPareto.txt");		
//		bw = null;		
//		PrintWriter salidaPareto = null;
//		if(!file.exists())
//		{
//			try {
//				fw = new FileWriter("D:/SolutionsPareto.txt");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			bw = new BufferedWriter(fw);
//			salidaPareto = new PrintWriter(bw);
//		}
//		else
//		{
//			try {
//				fw = new FileWriter("D:/SolutionsPareto.txt", true);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			bw = new BufferedWriter(fw);
//			salidaPareto = new PrintWriter(bw);
//		}*/
//		return salida;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleMultiobjectiveTabu(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		//Creando el fichero para guardar los resultados de cada iteraci�n
//		//String fileName = "D:/Resultados/MultiobjectiveTabu.txt";
//		PrintWriter salida = null;//CreateFile(idRoles, cantWorkers, fileName,objectiveFunctions, (TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//		//state.add(actualSolution);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		//Operador
//		GenerateNeighbourSolutionMultiTabu operator = new GenerateNeighbourSolutionMultiTabu(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setIdProject(idProject);
//		operator.setInitialGenerate(0);
//		operator.setIdRoles(idRoles);
//		
//		RoleWorkerState saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();
//		problem.setState(saGeneratedSol);
//		LinkedList<SolutionVO> solVOL = null;
//		List<State> solutionsFrente = new LinkedList<State>();
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//
//				//Contando el tiempo de ejecuci�n
//				long initialTime = System.currentTimeMillis();
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//				TabuSolutions.maxelements = 20;
//				
//				//Strategy.greedyValue = false;
//				List<State> allSolutions = new ArrayList<State>();
//				List<State> solutionsActual = new ArrayList<State>();
//
//				// Activar Generador
//				Strategy.getStrategy().saveListStates = false;
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(51*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.MultiobjectiveTabuSearch);
//				allSolutions = Strategy.getStrategy().listStates;
//				solutionsFrente = Strategy.getStrategy().listRefPoblacFinal;
//
//				long finalTime = System.currentTimeMillis();
//				long realTime = finalTime - initialTime;
//
//				GuardarComportamientoRestricciones(Strategy.generator.getType().name().toString()); 
//				
//				Strategy.destroyExecute();
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(solutionsActual != null)
//					solVOL = writeSolutionsMulti(salida, allSolutions, solutionsFrente, problem, otherParameters, objectiveFunctions, String.valueOf(realTime), "MultiobjectiveTabuSearch");
//				else
//					return null;
//			}	
//			return solutionsFrente;
//		}
//		else
//			return null;
//		
//	}
//
//	@SuppressWarnings({ "unchecked", "static-access" })
//	@Override
//	protected Collection handleSA(Long[] idRoles, Long[] cantWorkers, Map additionalParameters) throws Exception {
//		Long idProject = (Long)additionalParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		additionalParameters = updateMapOtherParam(additionalParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(additionalParameters));
//		
//		//Creando el fichero para guardar los resultados de cada iteraci�n
//		String fileName = "D:/Resultados/SA.txt";
//		PrintWriter salida = CreateFile(idRoles, cantWorkers, fileName,objectiveFunctions, (TypeSolutionMethod)additionalParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(additionalParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])additionalParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(additionalParameters);
//		String a = (String)params.get("tecnica");
//		Strategy.setStrategyVariable(a);
//		//Operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod(TypeSolutionMethod.FactoresPonderados);
//		problem.setTypeSolutionMethod((TypeSolutionMethod)additionalParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//		
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setIdProject(idProject);
//		operator.setInitialGenerate(0);
//		operator.setIdRoles(idRoles);
//		RoleWorkerState saGeneratedSol;
//		if (Strategy.StrategyVariable.equals("preservacion")){
//		lista = saConstruction.getFilteredWorkers();
//		 saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolutionModif();
//		}
//		else 
//		{saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();}
//		problem.setState(saGeneratedSol);
//		LinkedList<SolutionVO> solVOL = null;
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//				SimulatedAnnealing.alpha = 0.88;
//				SimulatedAnnealing.tinitial = 10.0;
//				SimulatedAnnealing.tfinal = 0.0;
//				SimulatedAnnealing.countIterationsT = 50;
//				
//				// Activar Generador
//				Strategy.getStrategy().calculateTime = true;
//				Strategy.getStrategy().saveListBestStates = true;
//				Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(30000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.SimulatedAnnealing);
//
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(Strategy.getStrategy().getBestState() != null){
//					solVOL = writeSolutions(salida, Strategy.getStrategy().listBest, Strategy.getStrategy().listRefPoblacFinal, problem, additionalParameters, objectiveFunctions, Strategy.getStrategy().getBestState(), Strategy.getStrategy().timeExecute, "SimulatedAnnealing");
//				}
//				else{
//					return null;
//				}
//				Strategy.destroyExecute();
//			}	
//			return solVOL;
//		}
//		else
//			return null;
//	}
//
//	@SuppressWarnings({ "unchecked", "static-access" })
//	@Override
//	protected Collection handleTabu(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		//Creando el fichero para guardar los resultados de cada iteraci�n
//		//String fileName = "D:/Resultados/TS.txt";
//		PrintWriter salida = null;//CreateFile(idRoles, cantWorkers, fileName,objectiveFunctions, (TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		//Operador
//		GenerateNeighbourSolutionTabu operator = new GenerateNeighbourSolutionTabu(workers);
//		operator.setActualSolution(actualSolution);
//		operator.setSearchArea(workers);
//		operator.setInitialGenerate(1);
//		
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setInitialGenerate(0);
//		RoleWorkerState saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();
//		problem.setState(saGeneratedSol);
//		LinkedList<SolutionVO> solVOL = null;
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//				TabuSolutions.maxelements = 10;
//				
//				// Activar Generador
//				Strategy.getStrategy().calculateTime = true;
//				Strategy.getStrategy().saveListBestStates = false;
//				Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(30000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.TabuSearch);
//
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(Strategy.getStrategy().getBestState() != null){
//					solVOL = writeSolutions(salida, Strategy.getStrategy().listBest, Strategy.getStrategy().listRefPoblacFinal, problem, otherParameters, objectiveFunctions, Strategy.getStrategy().getBestState(), Strategy.getStrategy().timeExecute, "TabuSearch");
//				}
//				else{
//					return null;
//				}
//				Strategy.destroyExecute();
//			}	
//			return solVOL;
//		}
//		else
//			return null;
//	}
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleWorkerToWorkerSolutionVO(Map otherParameters, Long idProject, org.andromda.teamsoft.vo.Solution solut, String evalStr, org.andromda.teamsoft.vo.RoleWorker rw, Long idElement) throws Exception {
//		//Calculando la nueva carga del rol
//		Project project = getProjectDao().load(idProject);
//		Worker element = getWorkerDao().load(idElement);
//		float newWorkL = 0;
//		boolean found1 = false;
//		DecimalFormat df = new DecimalFormat("0.####");
//		for(Iterator iProjectRoles = project.getProjectRoles().iterator(); iProjectRoles.hasNext() && !found1; )
//		{
//			ProjectRoles projectRoles = (ProjectRoles)iProjectRoles.next();
//			if(projectRoles.getRole().getId().equals(rw.getRole())){
//				newWorkL += projectRoles.getRoleLoad().getValue();
//				found1 = true;
//			}
//		}
//		String newWorkLoad = df.format(newWorkL);
//		//Calculando el �ndice de Competencia del trabajador
//		Float workerCost = (Float)otherParameters.get(rw.getRole().toString()+"_"+element.getId().toString());
//		Collection technicalCompImp = project.getTechnicalCompImportance();
//		Collection genericCompImportance = project.getGenericCompImportance();
//		float roleIndex = 0;
//		boolean found = false;
//		int countWorkers = 0;
//		float niveles = 0;
//		float sumatoria = 0;
//		countWorkers = countWorkers + 1;
//		roleIndex = 0L;
//		niveles = 0L;
//		if(workerCost == null)
//		{
//			if(otherParameters.get("competency")!=null)
//			{
//				for(Iterator itci = technicalCompImp.iterator(); itci.hasNext();)
//				{
//					TechCompImp technicalCompImportance = (TechCompImp)itci.next();
//
//					for(Iterator wi = element.getTechnicalCompetitionValue().iterator(); wi.hasNext();)
//					{
//						TechnicalCompetitionValue wtcv = (TechnicalCompetitionValue)wi.next();
//
//						if(technicalCompImportance.getRole().getId().equals(rw.getRole()) && technicalCompImportance.getTechnicalCompetition().equals(wtcv.getTechnicalCompetition()))
//						{
//							roleIndex = roleIndex + wtcv.getLevels().getLevels().getLevels().floatValue() * technicalCompImportance.getCompImportance().getLevels().floatValue();
//							niveles = niveles + technicalCompImportance.getCompImportance().getLevels().floatValue();
//						}
//					}
//				}
//
//				//for generic competency
//				for(Iterator igci = genericCompImportance.iterator(); igci.hasNext();)
//				{
//					GenericCompImportance gci = (GenericCompImportance)igci.next();
//					Iterator wi = element.getCompetitionValue().iterator();
//
//					found = false;
//					while(wi.hasNext() && !found)
//					{
//						CompetitionValue wcv = (CompetitionValue)wi.next();
//						if(gci.getRole().getId().equals(rw.getRole())&& gci.getCompetitionLevel().getGenCompetition().equals(wcv.getCompLevel().getGenCompetition()))
//
//						{
//							roleIndex += wcv.getCompLevel().getLevels().getLevels()*gci.getCompImportance().getLevels();
//							found = true;
//							niveles = niveles + gci.getCompImportance().getLevels().floatValue();
//
//						}
//					}
//				}
//				otherParameters.put(rw.getRole().toString()+"_"+element.getId().toString(), new Float(roleIndex/niveles));
//				sumatoria +=roleIndex/niveles;
//			}
//			else{
//				sumatoria = 0;
//			}
//		}
//		else
//			sumatoria=0;//workerCost.floatValue();
//
//
//		String compIndex = df.format(sumatoria);
//		float competitionIndex = Float.valueOf(compIndex);
//		//Buscando los niveles de competencia del trabajador
//		LinkedList<ContainerForWeb> genericCompetitions = new LinkedList<ContainerForWeb>(); 
//		Collection genericComp = ServiceLocator.instance().getCompetitionService().getCompetitionsInWorker(element.getId());
//		for (Iterator iterator = genericComp.iterator(); iterator
//		.hasNext();) {
//			ContainerForWeb container = (ContainerForWeb) iterator.next();
//			genericCompetitions.add(container);
//		}
//		LinkedList<ContainerForWeb> technicalCompetition = new LinkedList<ContainerForWeb>();
//		Collection techComp = ServiceLocator.instance().getTechnicalCompetitionService().getTechnicalCompetitionInWorker(element.getId());
//		for (Iterator iterator = techComp
//				.iterator(); iterator.hasNext();) {
//			ContainerForWeb container = (ContainerForWeb) iterator.next();
//			technicalCompetition.add(container);
//		}
//		//Obtener el �ndice de experiencia
//		float expIndex = 0;
//		Collection exps;
//		boolean existRoleExperience;
//		Float expData = (Float)otherParameters.get(rw.getRole().toString()+"_exp_"+element.getId().toString());
//		if(expData == null)
//		{
//			RoleExperience roleExperience = null;
//			exps = element.getRoleExperiences();
//			Iterator it = exps.iterator();
//			existRoleExperience = false;
//			while(it.hasNext() && !existRoleExperience)
//			{
//				RoleExperience re = (RoleExperience)it.next();
//				if(re.getRole().getId().equals(rw.getRole()))
//				{
//					roleExperience = re;
//					existRoleExperience = true;
//				}
//			}
//			if(roleExperience != null)
//			{
//				expIndex += roleExperience.getIndexes();
//				otherParameters.put(rw.getRole().toString()+"_exp_"+element.getId().toString(), new Float(roleExperience.getIndexes()));
//			}
//		}
//		else
//			expIndex += expData.floatValue();
//		String expI = df.format(expIndex);
//		float experienceIndex = Float.valueOf(expI);
//		//Obtener la evaluaci�n en otros proyectos en los que ha desempe�ado el rol
//		Collection projectEval = ServiceLocator.instance().getRoleExperienceService().getRoleEvaluations(rw.getRole(), element.getId());
//		LinkedList<ProjectRoleEvaluationVO> projectEvaluation = new LinkedList();
//		for (Iterator iterator = projectEval.iterator(); iterator
//		.hasNext();) {
//			RoleEvaluationVO roleEvalVO = (RoleEvaluationVO) iterator.next();
//			ProjectRoleEvaluationVO projRoleEvalVO = new ProjectRoleEvaluationVO(roleEvalVO.getId(), roleEvalVO.getIndex(), getRoleEvaluationDao().load(roleEvalVO.getId()).getProject().getProjectName());
//			projectEvaluation.add(projRoleEvalVO);
//		}
//		//Buscando el tipo MBTI
//		String mbtiType = "";
//		WorkerTestVO workerTestVO = ServiceLocator.instance().getWorkerTestService().getWorkerTest(element.getId());
//		if(workerTestVO != null){
//			String mbti = workerTestVO.getTipoMB();
//			if(mbti.substring(0, 1) == "E"){
//				mbtiType += "Extrovertido";
//			}
//			else{
//				mbtiType += "Introvertido";
//			}
//			if(mbti.substring(1, 2) == "N"){
//				mbtiType += "/Intuitivo";
//			}
//			else{
//				mbtiType += "/Sensitivo";
//			}
//			if(mbti.substring(2, 3) == "T"){
//				mbtiType += "/Objetivo";
//			}
//			else{
//				mbtiType += "/Sentimental";
//			}
//			if(mbti.substring(3, 4) == "J"){
//				mbtiType += "/Planificado";
//			}
//			else{
//				mbtiType += "/Expont�neo";
//			}
//		}
//		//Obteniendo los roles de Belbin preferidos
//		LinkedList<String> prefBelbinRoles = new LinkedList<String>();
//
//		Collection prefBelbin = ServiceLocator.instance().getWorkerTestService().getPreferRoles(element.getId());//Devulelve un listado de string
//		for (Iterator iterator = prefBelbin.iterator(); iterator
//		.hasNext();) {
//			String prefB = (String) iterator.next();
//			prefBelbinRoles.add(prefB);
//		}
//		//Obteniendo los roles de Belbin Evitados
//		LinkedList<String> avoidedBelbinRoles = new LinkedList<String>(); 
//		Collection avoidBelbin = ServiceLocator.instance().getWorkerTestService().getAvoidedRoles(element.getId());
//		for (Iterator iterator = avoidBelbin.iterator(); iterator
//		.hasNext();) {
//			String avoidB = (String) iterator.next();
//			avoidedBelbinRoles.add(avoidB);
//		}
//		//Obteniendo el costo de un desarrollo a distancia
//		CostDistanceVO costDistVO = ServiceLocator.instance().getCountyService().getCostDistance(project.getProvince().getId(), element.getMunicipality().getCounty().getId());
//		float costDistance = 0;
//		if(costDistVO != null){
//			costDistance = costDistVO.getCostDistance();
//		} 
//		else
//			costDistance = 0;
//		float cost = getRoleDao().load(rw.getRole()).getImpact()*costDistance;
//
//		//Obteniendo la provincia del trabajador
//		CountyVO province = ServiceLocator.instance().getCountyService().getCounty(element.getMunicipality().getCounty().getId());
//
//		//Obteniendo el �ndice de incompatibilidad de la persona
//		LinkedList<Worker> allWorkers = new LinkedList<Worker>();
//		Worker worker;
//		Collection solutions = (LinkedList<RoleWorker>) solut.getSolutions();
//		for (Iterator iter = solutions.iterator(); iter.hasNext();) {
//			LinkedList<RoleWorker> elements = (LinkedList<RoleWorker>) iter.next();
//			for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
//				RoleWorker roleWorker = (RoleWorker) iterator.next();
//				for(int j=0; j<roleWorker.getWorkers().size();j++)
//				{
//					worker = (Worker)roleWorker.getWorkers().get(j);
//					if(!allWorkers.contains(worker))
//					{
//						allWorkers.add(worker);
//					}
//				}
//			}
//		}
//		Worker innerWorker;
//		boolean found2 = false;
//		int sum = 0;
//		for(int inner = 0; inner<allWorkers.size(); inner++)
//		{
//			innerWorker = (Worker)allWorkers.get(inner);
//			found2 = false;
//			for(Iterator wCIter = element.getConflictList().iterator(); wCIter.hasNext() && !found2;)
//			{
//				WorkerConflict wc = (WorkerConflict)wCIter.next();
//				if(wc.getWorkerConflict().equals(innerWorker))
//				{
//					sum++;
//					found2 = true;
//				}
//			}
//		}
//		float workerIncompatibilitiesIndex = 0;
//		if(allWorkers.size() > 1){
//			workerIncompatibilitiesIndex =  (float)(sum/(allWorkers.size()-1));
//		}
//		if(rw.getFixedWorkers().contains(element))
//			element.setIdCard("FIXED");
//
//		WorkerSolutionVO workerSolutionVO = new WorkerSolutionVO(element.getId(), element.getWorkerName(), element.getIdCard(), element.getLastName(), element.getSurName(), element.getAddress(), element.getPhone(), element.getSex(), element.getEmail(), element.getLogin(), element.getKeyWord(), element.getInDate(), element.getState(), element.getInstitution().getId(), element.getGraduationDate(), element.getFax(), element.getId()+11000, element.getId()+12000, Float.valueOf(df.format(element.getWorkload())), element.getStatus(), element.getId()+13000, Float.valueOf(newWorkLoad), element.getId()+1000, competitionIndex, element.getId()+2000, genericCompetitions, element.getId()+3000, technicalCompetition, experienceIndex, element.getId()+4000, projectEvaluation, element.getId()+45000, mbtiType, element.getId()+6000, element.getId()+7000, prefBelbinRoles, element.getId()+8000, avoidedBelbinRoles, element.getId()+9000, cost, province, element.getId()+10000, workerIncompatibilitiesIndex);
//		Collection aux = new LinkedList<WorkerSolutionVO>();
//		aux.add(workerSolutionVO);
//		return aux;
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	protected boolean handleExistWorkersForRoles(Long[] idRoles, Long[] cantNeededWorkers, Map otherParameters, String[] idWorkers)
//	throws Exception {
//
//		Long idProject = (Long)otherParameters.get("idProject");
//		@SuppressWarnings("unused")
//		Project project = getProjectDao().load(idProject);
//		otherParameters = updateMapOtherParam(otherParameters);
//		Collection workers = getSearchArea();
//		LinkedList objectiveFunctions = new LinkedList();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		boolean[] availableWorkers = new boolean[idRoles.length];
//
//
//		ProblemX problem = null; //new ProblemX(project, idRoles,cantNeededWorkers,objectiveFunctions ,restrictions, otherParameters, ((Integer)otherParameters.get("solutionMethod")));
//
//
//
//		State sol = (State)getInitialSolution(idRoles, cantNeededWorkers, idWorkers, idProject);
//
//
//
//		LinkedList rwTempList = new LinkedList();
//		LinkedList rclTemp = new LinkedList();
//		RoleWorker rw;
//
//		for(int i=0; i<problem.getIdRoles().length; i++) 
//		{
//			if(problem.getIdRoles()[i]!=null)
//			{
//				rwTempList = new LinkedList();
//				RoleWorker roleWorker = null;
//
//				@SuppressWarnings("unused")
//				boolean found = false;
//				for (int j = 0; j < sol.getCode().size(); j++) {
//					RoleWorker temp = (RoleWorker)sol.getCode().get(j);
//					if(((Role)temp.getRole()).getId().equals(problem.getIdRoles()[i]))
//					{
//						found = true;
//						roleWorker = temp;
//					}
//				}
//				if(roleWorker!=null)
//				{
//					rw = roleWorker;
//					rwTempList = rw.getWorkers();
//				}
//				else
//				{
//					rw = new RoleWorker(getRoleDao().load(problem.getIdRoles()[i]), rwTempList,problem.getCantWorkers()[i]);
//					sol.getCode().add(i,rw);
//				}
//				boolean quota = false;
//				rclTemp.clear();
//
//				for(Iterator iter = workers.iterator(); iter.hasNext() && !quota;)
//				{
//					Worker w = (Worker)iter.next();
//					rw.getWorkers().add(w);
//					if(problem.getCodification().validState(sol))
//					{
//						rclTemp.add(new WorkerCost(w, 0));
//						if(rclTemp.size()>=rw.getNeededWorkers())
//							availableWorkers[i] = true;
//					}
//					rw.getWorkers().remove(w);
//				}
//				rw.setWorkers(rwTempList);
//			}
//		}
//		boolean allTrue = true;
//		int x = 0;
//		while(x<availableWorkers.length && !allTrue)
//		{
//			if(availableWorkers[x]==false)
//				allTrue = false;
//			x++;
//		}
//
//		return allTrue;
//	}
//
//	@Override
//	protected Collection handleDistributionEstimationAlgorithm(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		return null;
//	}
//	@SuppressWarnings({ "unchecked", "static-access" })
//	@Override
//	protected Collection handleEvolutionStrategies(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		//Creando el fichero para guardar los resultados de cada iteraci�n
//		//String fileName = "D:/Resultados/EvolutionStrategies.txt";
//		PrintWriter salida = null;//CreateFile(idRoles, cantWorkers, fileName,objectiveFunctions, (TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		//Operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//		
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//		
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setInitialGenerate(0);
//		RoleWorkerState saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();
//		problem.setState(saGeneratedSol);
//		problem.setCodification(solution);
//		LinkedList<SolutionVO> solVOL = null;
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//				EvolutionStrategies.countRef = 10;
//				EvolutionStrategies.truncation = (EvolutionStrategies.countRef * 50) / 100;
//				EvolutionStrategies.PM = 0.4;
//				EvolutionStrategies.selectionType = SelectionType.TruncationSelection;
//				EvolutionStrategies.mutationType = MutationType.OnePointMutation;
//				EvolutionStrategies.replaceType = ReplaceType.GenerationalReplace;
//				
//				// Activar Generador
//				Strategy.getStrategy().calculateTime = true;
//				Strategy.getStrategy().saveListBestStates = false;
//				Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
//				Strategy.getStrategy().validate = true;
//				Strategy.getStrategy().posibleValidateNumber = 100;
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(30000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.RandomSearch);
//				EvolutionStrategies.countRef = 0;
//				
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(Strategy.getStrategy().getBestState() != null){
//					solVOL = writeSolutions(salida, Strategy.getStrategy().listBest, Strategy.getStrategy().listRefPoblacFinal, problem, otherParameters, objectiveFunctions, Strategy.getStrategy().getBestState(), Strategy.getStrategy().timeExecute, "EvolutionStrategies");
//				}
//				else{
//					return null;
//				}
//				Strategy.destroyExecute();
//			}	
//			return solVOL;
//		}
//		else
//			return null;
//	}
//	@SuppressWarnings({ "unchecked", "static-access" })
//	@Override
//	protected Collection handleGeneticAlgorithm(Long[] idRoles, Long[] cantWorkers, Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		//Creando el fichero para guardar los resultados de cada iteraci�n
//		//String fileName = "D:/Resultados/GeneticAlgorithm.txt";
//		PrintWriter salida = null;//CreateFile(idRoles, cantWorkers, fileName,objectiveFunctions, (TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		//Operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//		
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		//problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
//		
//		//Generando una soluci�n inicial
//		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setInitialGenerate(0);
//		RoleWorkerState saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();
//		problem.setState(saGeneratedSol);
//		problem.setCodification(solution);
//		LinkedList<SolutionVO> solVOL = null;
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//				GeneticAlgorithm.countRef = 5;
//				GeneticAlgorithm.truncation = (GeneticAlgorithm.countRef * 50) / 100;
//				GeneticAlgorithm.PM = 0.9; //ResourceBundle.getBundle("/Bundle_es_ES").getString(key);
//				GeneticAlgorithm.PC = 0.5;
//				GeneticAlgorithm.selectionType = SelectionType.TruncationSelection;
//				GeneticAlgorithm.crossoverType = CrossoverType.UniformCrossover;
//				GeneticAlgorithm.mutationType = MutationType.OnePointMutation;
//				GeneticAlgorithm.replaceType = ReplaceType.SteadyStateReplace;
//				
//				// Activar Generador
//				Strategy.getStrategy().calculateTime = true;
//				Strategy.getStrategy().saveListBestStates = false;
//				Strategy.getStrategy().saveFreneParetoMonoObjetivo = true;
//				Strategy.getStrategy().validate = true;
//				Strategy.getStrategy().posibleValidateNumber = 100;
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(30000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.RandomSearch);
//				GeneticAlgorithm.countRef = 0;
//				
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(Strategy.getStrategy().getBestState() != null){
//					solVOL = writeSolutions(salida, Strategy.getStrategy().listBest, Strategy.getStrategy().listRefPoblacFinal, problem, otherParameters, objectiveFunctions, Strategy.getStrategy().getBestState(), Strategy.getStrategy().timeExecute, "GeneticAlgorithm");
//				}
//				else{
//					return null;
//				}
//				Strategy.destroyExecute();
//			}	
//			return solVOL;
//		}
//		else
//			return null;
//	}
//
//	@Override
//	protected boolean handleListDominance(Object solution, List solutions)
//	throws Exception {
//		return false;
//	}
//	@SuppressWarnings("unchecked")
//	@Override
//	protected Collection handleNSGAII(Long[] idRoles, Long[] cantWorkers,
//			Map otherParameters) throws Exception {
//		Long idProject = (Long)otherParameters.get("idProject");
//		Project project = getProjectDao().load(idProject);
//		//Mapa de par�metros
//		otherParameters = updateMapOtherParam(otherParameters);
//		//Area de busqueda
//		Collection workers = getSearchArea();
//		//Funciones objetivo
//		ArrayList<ObjetiveFunction> objectiveFunctions = new ArrayList<ObjetiveFunction>();
//		objectiveFunctions.addAll(getObjectiveFunctions(otherParameters));
//		
//		//String fileName = "D:/Resultados/NSGAII.txt";
//		PrintWriter salida = null;//CreateFile(idRoles, cantWorkers, fileName, objectiveFunctions, (TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		//Restricciones
//		LinkedList restrictions = new LinkedList();
//		restrictions.addAll(getRestrictions(otherParameters));
//		//Trabajadores fijos
//		String[] idWorkers = (String[])otherParameters.get("fixedWorkers");
//		if(idWorkers == null)
//			idWorkers = new String[1];
//		//Soluci�n inicial
//		RoleWorkerState actualSolution = (RoleWorkerState) getInitialSolution(idRoles, cantWorkers, idWorkers, idProject);
//		//state.add(actualSolution);
//
//		Map params = new TreeMap<Object, Object>();
//		params.putAll(otherParameters);
//		//Operador
//		GenerateNeighbourSolution operator = new GenerateNeighbourSolution(workers);
//		operator.setCantNeededWorkers(cantWorkers);
//		operator.setIdWorkers(idWorkers);
//		operator.setActualSolution(actualSolution);
//
//		//Problema
//		ProblemX problem = new ProblemX(idRoles, cantWorkers, project, params);
//		problem.setOperator(operator);
//		problem.setState(actualSolution);
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		problem.setTypeSolutionMethod((TypeSolutionMethod)otherParameters.get("solutionMethod"));
//		
//		problem.setTypeProblem(ProblemType.Maximizar);
//		problem.setFunction(objectiveFunctions);
//		operator.setProblem(problem);
//		Solution solution = new Solution(restrictions,problem,workers);
////		//Generando una soluci�n inicial
////		RandomConstruction saConstruction = new RandomConstruction(actualSolution, problem, workers,solution);
//		operator.setSolution(solution);
//		operator.setIdProject(idProject);
//		operator.setInitialGenerate(0);
//		operator.setIdRoles(idRoles);
//		RoleWorkerState saGeneratedSol = (RoleWorkerState) saConstruction.getAleatorySolution();
//		problem.setState(saGeneratedSol);
//		problem.setCodification(solution);
//		LinkedList<SolutionVO> solVOL = null;
//		List<State> solutionsFrente = new LinkedList<State>();
//		if(saGeneratedSol != null){
//			if(solution.isTeamComplete(saGeneratedSol) == true){
//				solVOL = new LinkedList<SolutionVO>();
//
//				//Contando el tiempo de ejecuci�n
//				long initialTime = System.currentTimeMillis();
//				StopExecute stopExecute = new StopExecute();
//
//				//Configurando la biblioteca
//				Strategy.getStrategy().setStopexecute(stopExecute);
//				Strategy.getStrategy().setProblem(problem);
//				UpdateParameter updateParameter = new UpdateParameter();
//				Strategy.getStrategy().setUpdateparameter(updateParameter);
//				NSGAII.countRef = 5;
//				NSGAII.PM = 0.9;
//				NSGAII.PC = 0.5;
//				NSGAII.selectionType = SelectionType.TournamentSelection;
//				NSGAII.crossoverType = CrossoverType.UniformCrossover;
//				NSGAII.mutationType = MutationType.OnePointMutation;
//				NSGAII.replaceType = ReplaceType.SteadyStateReplace;
//				NSGAII.listState = new ArrayList<State>();
//				RandomSearch.listStateReference = new ArrayList<State>();
//				
//				//Strategy.greedyValue = false;
//				List<State> allSolutions = new ArrayList<State>();
//				List<State> solutionsActual = new ArrayList<State>();
//
//				// Activar Generador
//				Strategy.getStrategy().saveListBestStates = false;
//				Strategy.getStrategy().saveListStates = false;
//				Strategy.getStrategy().validate = true;
//				Strategy.getStrategy().posibleValidateNumber = 100;
//				Strategy.getStrategy().saveListStates = false;
//				DecimalFormat df = new DecimalFormat("0.####");
//				String p = df.format(6000*workers.size()/77);
//				int iterations = Integer.valueOf(p);
//				
//				Strategy.getStrategy().executeStrategy(iterations, 1,  GeneratorType.RandomSearch);
//				allSolutions = Strategy.getStrategy().listStates;
//				solutionsFrente = Strategy.getStrategy().listRefPoblacFinal;
//				NSGAII.countRef = 0;
//				
//				long finalTime = System.currentTimeMillis();
//				long realTime = finalTime - initialTime;
//
//				GuardarComportamientoRestricciones(Strategy.generator.getType().name().toString()); 
//				
//				Strategy.destroyExecute();
//				//Mostrando la mejor soluci�n y guardando soluciones en el fichero de texto
//				if(solutionsActual != null)
//					solVOL = writeSolutionsMulti(salida, allSolutions, solutionsFrente, problem, otherParameters, objectiveFunctions, String.valueOf(realTime), "NSGAII");
//				else
//					return null;
//			}	
//			return solutionsFrente;
//		}
//		else
//			return null;
//	}
//	
//	//Para analizar el incumplimiento de las restricciones
//	protected void GuardarComportamientoRestricciones(String algorithm_name){
//		@SuppressWarnings("unused")
//		ArrayList<Integer> listresult = new ArrayList<Integer>();
//
//		FileWriter fw = null;
//		File file = new File("D:/Resultados/Restricciones-" + algorithm_name + ".txt");		
//		BufferedWriter bw = null;		
//		PrintWriter salida = null;
//		if(!file.exists())
//		{
//			try {
//				fw = new FileWriter("D:/Resultados/Restricciones-" + algorithm_name + ".txt");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			bw = new BufferedWriter(fw);
//			salida = new PrintWriter(bw);
//		}
//		else
//		{
//			try {
//				fw = new FileWriter("D:/Resultados/Restricciones-" + algorithm_name + ".txt", true);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			bw = new BufferedWriter(fw);
//			salida = new PrintWriter(bw);
//		}
//		
//		salida.println("Existe cerebro;" + Strategy.existCerebro + ";" + "No;" + Strategy.notExistCerebro);
//		salida.println("Se cumple carga m�xima de trabajo;" + Strategy.maxWorkload + ";" + "No;" + Strategy.notMaxWorkload);
//		salida.println("Se cumple cantidad m�xima de roles;" + Strategy.maximumRoles + ";" + "No;" + Strategy.notMaximumRoles);
//		salida.println("Existen todas las categorias de Belbin;" + Strategy.allBelbinCategories + ";" + "No;" + Strategy.notAllBelbinCategories);
//		salida.println("Existen todos los roles de Belbin;" + Strategy.allBelbinRoles + ";" + "No;" + Strategy.notAllBelbinRoles);
//		salida.println("Existen todos los niveles de competencia;" + Strategy.allCompetitionLevel + ";" + "No t�cnicas;" + Strategy.notAllTechnicalCompetitionLevel + ";" + "No gen�ricas;" + Strategy.notAllGenericCompetitionLevel);
//		salida.println("No existen roles incompatibles;" + Strategy.incompatibleRoles + ";" + "No;" + Strategy.notIncompatibleRoles);
//		salida.println("No existen trabajadores seleccionados incompatibles;" + Strategy.incompatibleSelectedWorkers + ";" + "No;" + Strategy.notIncompatibleSelectedWorkers);
//		salida.println("No existen trabajadores incompatibles;" + Strategy.incompatibleWorkers + ";" + "No;" + Strategy.notIncompatibleWorkers);
//		salida.println("Tiene experiencia en proyectos de igual complejidad;" + Strategy.complexityExperience + ";" + "No;" + Strategy.notComplexityExperience);
//		salida.println("Existe trabajador asignado;" + Strategy.isWorkerAssigned + ";" + "No;" + Strategy.notIsWorkerAssigned);
//		salida.println("No es jefe de proyecto;" + Strategy.isProjectBoss + ";" + "No;" + Strategy.notIsProjectBoss);
//		salida.println("Tiene roles de Belbin IS y CO;" + Strategy.isISCO + ";" + "No;" + Strategy.notIsISCO);
//		salida.println("Tiene subtipo E_ _J;" + Strategy.isEJ + ";" + "No;" + Strategy.notIsEJ);
//		salida.println("Tiene experiencia en el dominio del proyecto;" + Strategy.isDomainExperience + ";" + "No;" + Strategy.notIsDomainExperience);
//		salida.println("No existen trabajadores repetidos en el mismo rol;" + Strategy.workerNotRepeatedInSameRole + ";" + "No;" + Strategy.notWorkerNotRepeatedInSameRole);
//		salida.println("No existen trabajadores eliminados en ese rol;" + Strategy.workerRemovedFromRole + ";" + "No;" + Strategy.notWorkerRemovedFromRole);
//		salida.println("Roles de accion sobre roles mentales;" + Strategy.actionMentalGlobal + ";" + "Roles mentales sobre roles sociales;" + Strategy.mentalSocialGlobal + ";" + "Incumple ambas;" + Strategy.ambas + ";" + "Esta balanceado;" + Strategy.isBalanced);
//		
//		salida.close();
//	}
}