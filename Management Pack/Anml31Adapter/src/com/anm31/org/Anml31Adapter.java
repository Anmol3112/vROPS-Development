
/*
 * Copyright (c) 2014-2018 VMware, Inc. All rights reserved.
 */
package com.anm31.org;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.integrien.alive.common.adapter3.Logger;
import com.integrien.alive.common.adapter3.MetricData;
import com.integrien.alive.common.adapter3.MetricKey;
import com.integrien.alive.common.adapter3.Relationships;
import com.integrien.alive.common.adapter3.ResourceKey;
import com.integrien.alive.common.adapter3.AdapterBase;
import com.integrien.alive.common.adapter3.DiscoveryParam;
import com.integrien.alive.common.adapter3.DiscoveryResult;
import com.integrien.alive.common.adapter3.ResourceStatus;
import com.integrien.alive.common.adapter3.TestParam;
import com.integrien.alive.common.adapter3.config.ResourceConfig;
import com.integrien.alive.common.adapter3.config.ResourceIdentifierConfig;
import com.integrien.alive.common.adapter3.describe.AdapterDescribe;
import com.anm31.org.util.Anml31AdapterUtil;

/**
 * Main adapter class for Anml31Adapter
 */
public class Anml31Adapter extends AdapterBase {

	private final Logger logger;

	private Anml31AdapterUtil adapterUtil;

	/**
	 * Default constructor
	 */
	public Anml31Adapter() {
		this(null, null);
	}

	/**
	 * Parameterized constructor
	 *
	 * @param instanceName
	 *                     instance name
	 * @param instanceId
	 *                     instance Id
	 */
	public Anml31Adapter(String instanceName, Integer instanceId) {
		super(instanceName, instanceId);
		logger = adapterLoggerFactory.getLogger(Anml31Adapter.class);
		adapterUtil = new Anml31AdapterUtil(adapterLoggerFactory);
	}

	/**
	 * The responsibility of the method is to provide adapter describe
	 * information to the collection framework.
	 */
	@Override
	public AdapterDescribe onDescribe() {
		logger.info("Inside onDescribe method of Anml31Adapter class");
		return adapterUtil.createAdapterDescribe();
	}

	/**
	 * This method is called when user wants to discover resources in the target
	 * system manually. onConfigure is not called before onDiscover.
	 */
	@Override
	public DiscoveryResult onDiscover(DiscoveryParam discParam) {

		logger.info("Inside onDiscover method of Anml31Adapter class");
		DiscoveryResult discoveryResult = new DiscoveryResult(discParam.getAdapterInstResource());

		return discoveryResult;
	}

	/**
	 * This method is called for each collection cycle. By default, this value
	 * is 5 minutes unless user changes it
	 */
	@Override
	public void onCollect(ResourceConfig adapterInstResource,
			Collection<ResourceConfig> monitoringResources) {

		if (logger.isInfoEnabled()) {
			logger.info("Inside onCollect method of Anml31Adapter class");
		}

		collectResult.getDiscoveryResult(true);

		DiscoveryResult discoveryResult = new DiscoveryResult(adapterInstResource);
		collectResult.setDiscoveryResult(discoveryResult);

		ResourceKey vCenterResource = getVC();
		addResource(discoveryResult, vCenterResource);
		addMetricData(monitoringResources, vCenterResource);

		ResourceKey EsxiResource = getESXI();
		addResource(discoveryResult, EsxiResource);
		addMetricData(monitoringResources, EsxiResource);
		
		ResourceKey DataResource = getdata();
		addResource(discoveryResult, DataResource);
		addMetricData(monitoringResources, DataResource);
		
		ResourceKey VmResource = getVM();
		addResource(discoveryResult, VmResource);
		addMetricData(monitoringResources, VmResource);
		
		//different list for every ResourceKey

		List<ResourceKey> adapterInstanceChildren = new ArrayList<>();
		adapterInstanceChildren.add(vCenterResource);
		
	    List<ResourceKey> vCenterChildren = new ArrayList<>();
		 vCenterChildren.add(EsxiResource);
		
     	List<ResourceKey> EsxiChildren = new ArrayList<>();
		 EsxiChildren.add(VmResource);
 
		List<ResourceKey> VmChildren = new ArrayList<>();
		 VmChildren.add(DataResource);
		 
	    //Relationships
		 
		Relationships instaceRelationships = new Relationships();
		instaceRelationships.setRelationships(adapterInstResource.getResourceKey(), adapterInstanceChildren);
		discoveryResult.addRelationships(instaceRelationships);
		
		
		 Relationships vCenterRelationships = new Relationships();
		  vCenterRelationships.setRelationships(vCenterResource,vCenterChildren);
	      discoveryResult.addRelationships(vCenterRelationships);
		  
		  Relationships EsxiRelationships = new Relationships();
		  EsxiRelationships.setRelationships(EsxiResource,EsxiChildren);
		  discoveryResult.addRelationships(EsxiRelationships);
		  
		  Relationships VmRelationships = new Relationships();
		  VmRelationships.setRelationships(VmResource, VmChildren);
		  discoveryResult.addRelationships(VmRelationships);
		 
		
		

	}

	private ResourceConfig addMetrics(ResourceConfig resourceConfig, String name) {

		long curTime = System.currentTimeMillis();
		String metricGroup1 = "Capacity";
        
		//Vcenter Metrics

		MetricKey nameMetricKey1 = new MetricKey();
		nameMetricKey1.setProperty(true);
		nameMetricKey1.add(metricGroup1).add("vCenterName");
		MetricData metricData1 = new MetricData(nameMetricKey1, curTime, "vCenterHost");
		addMetricData(resourceConfig, metricData1);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = vCenterHost");

		MetricKey nameMetricKey2 = new MetricKey();
		nameMetricKey2.setProperty(false);
		nameMetricKey2.add(metricGroup1).add("cpu_usage");
		MetricData metricData2 = new MetricData(nameMetricKey2, curTime, 91);
		addMetricData(resourceConfig, metricData2);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = 91");

		MetricKey nameMetricKey3 = new MetricKey();
		nameMetricKey3.setProperty(false);
		nameMetricKey3.add(metricGroup1).add("network_usage");
		MetricData metricData3 = new MetricData(nameMetricKey3, curTime, 76);
		addMetricData(resourceConfig, metricData3);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = 76");

		/* esxi kind key */

		String esxiMetricGroup = "Performance";

		MetricKey esxiMetricKey1 = new MetricKey();
		esxiMetricKey1.setProperty(false);
		esxiMetricKey1.add(esxiMetricGroup).add("cpu_ready");
		MetricData esximetricData = new MetricData(esxiMetricKey1, curTime, 452);
		addMetricData(resourceConfig, esximetricData);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = 452");

		MetricKey esxiMetricKey2 = new MetricKey();
		esxiMetricKey2.setProperty(false);
		esxiMetricKey2.add(esxiMetricGroup).add("cpu_contentation");
		MetricData esximetricData2 = new MetricData(esxiMetricKey2, curTime, 0.214);
		addMetricData(resourceConfig, esximetricData2);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = 0.214");

		
		//datastore
		
		String DataMetricGroup="datastore_details";
		
		MetricKey dataMetricKey1=new MetricKey();
		dataMetricKey1.setProperty(false);
		dataMetricKey1.add(DataMetricGroup).add("DataStoreLatency");
		MetricData DataMetricData1=new MetricData(dataMetricKey1,curTime,2);
		addMetricData(resourceConfig,DataMetricData1);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = 2");
		
		MetricKey dataMetricKey2=new MetricKey();
		dataMetricKey2.setProperty(false);
		dataMetricKey2.add(DataMetricGroup).add("DataStoreIOPS");
		MetricData DataMetricData2=new MetricData(dataMetricKey2,curTime,"iops");
		addMetricData(resourceConfig,DataMetricData2);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = 2");
		
		MetricKey dataMetricKey3=new MetricKey();
		dataMetricKey3.setProperty(false);
		dataMetricKey3.add(DataMetricGroup).add("DataStoreUsages");
		MetricData DataMetricData3=new MetricData(dataMetricKey3,curTime,50);
		addMetricData(resourceConfig,DataMetricData3);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = 50");
		
		
        //	  VM metrics
		
        String VMMetricGroup="VmAnmolDetails";
		
		MetricKey VMMetricKey1=new MetricKey();
		VMMetricKey1.setProperty(false);
		VMMetricKey1.add(VMMetricGroup).add("TotalCapacity");
		MetricData VMMetricData1=new MetricData(VMMetricKey1,curTime,1024);
		addMetricData(resourceConfig,VMMetricData1);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = 1024");
		
		MetricKey VmMetricKey2=new MetricKey();
		VmMetricKey2.setProperty(false);
		VmMetricKey2.add(VMMetricGroup).add("FreeCapacity");
		MetricData VmMetricData2=new MetricData(VmMetricKey2,curTime,576);
		addMetricData(resourceConfig,VmMetricData2);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = 576");
		
		MetricKey VmMetricKey3=new MetricKey();
		VmMetricKey3.setProperty(false);
		VmMetricKey3.add(VMMetricGroup).add("PowerState");
		MetricData VmMetricData3=new MetricData(VmMetricKey3,curTime,"ON");
		addMetricData(resourceConfig,VmMetricData3);
		logger.info("Adding metric data for resource = " + resourceConfig.getResourceName() + " and metricData = ON");
		
		
		return resourceConfig;

	}

	private void addMetricData(Collection<ResourceConfig> monitoringResources, ResourceKey vCenterResource) {

		if (monitoringResources.size() != 0) {
			for (ResourceConfig resourceConfig : monitoringResources) {
				if (resourceConfig == null) {
					logger.warn("Warning: Null resource in monitoring resources list. Will skip it.");
					continue;
				}
				if (resourceConfig.getResourceKey().equals(vCenterResource)) {
					addMetrics(resourceConfig, "demo1");
				} else {
					addMetrics(resourceConfig, "demo2");
				}
			}
		}
	}

	public ResourceKey getVC() {
		ResourceKey vcreskey = new ResourceKey("vcenter_details", "AnmolvCenter", "Anml31Adapter");
		ResourceIdentifierConfig ric = new ResourceIdentifierConfig("id", "vcenter_details", true);
		vcreskey.addIdentifier(ric);
		return vcreskey;
	}

	public ResourceKey getESXI() {
		ResourceKey esxireskey = new ResourceKey("172.142.14.4", "AnmolEsxiHost", "Anml31Adapter");
		ResourceIdentifierConfig ric = new ResourceIdentifierConfig("id1", "172.142.14.4", true);
		esxireskey.addIdentifier(ric);
		return esxireskey;
	}
	public ResourceKey getdata() {
		ResourceKey datastorereskey = new ResourceKey("datastore_anmol", "AnmolDataStore", "Anml31Adapter");
		ResourceIdentifierConfig ric = new ResourceIdentifierConfig("id2", "datastore_anmol", true);
		datastorereskey.addIdentifier(ric);
		return datastorereskey;
	}
	
	public ResourceKey getVM() {
		ResourceKey VMreskey = new ResourceKey("VmAnmolDetails", "AnmolVm", "Anml31Adapter");
		ResourceIdentifierConfig ric = new ResourceIdentifierConfig("id3", "VmAnmolDetails", true);
		VMreskey.addIdentifier(ric);
		return VMreskey;
	}

	private void addResource(DiscoveryResult discoveryResult, ResourceKey resourceKey) {

		if (isNewResource(resourceKey)) {
			if (isNewResourceKind(resourceKey.getAdapterKind(), resourceKey.getResourceKind())) {
				if (logger.isDebugEnabled()) {
					logger.debug("Creating new resource kind for resource " + resourceKey);
				}
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Creating new resource " + resourceKey);
			}
			discoveryResult.addResource(new ResourceConfig(resourceKey));
		}
	}

	/**
	 * This method is called when a new adapter instance is created.
	 */
	@Override
	public void onConfigure(ResourceStatus resStatus,
			ResourceConfig adapterInstResource) {

		// TODO get the adapter instance properties(IdentifierCredentialProperties)
		// and use it as part of the onCollect
		// final IdentifierCredentialProperties prop =
		// new IdentifierCredentialProperties(loggerFactory, adapterInstResource);

		if (logger.isInfoEnabled()) {
			logger.info("Inside onConfigure method of Anml31Adapter class");
		}
	}

	/**
	 * This method is called when user presses "Test" button while
	 * creating/editing an adapter instance.
	 */
	@Override
	public boolean onTest(TestParam testParam) {

		// TODO get the adapter instance properties(IdentifierCredentialProperties)
		// and perform a test.
		// final IdentifierCredentialProperties prop =
		// new IdentifierCredentialProperties(loggerFactory, adapterInstResource);

		if (logger.isInfoEnabled()) {
			logger.info("Inside onTest method of Anml31Adapter class");
		}
		return true;
	}
}