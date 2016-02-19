package br.inpe.cap.interfacemetrics.interfaces.daemon;

import br.inpe.cap.interfacemetrics.application.InterfaceMetricsService;
import br.inpe.cap.interfacemetrics.infrastructure.util.LogUtils;

public class Main {

	private static long startTime = -1;
	
	public static void main(String[] args) {
		setStartTime();
		
		LogUtils.getLogger().info("");
		LogUtils.getLogger().info("Aplicativo iniciado");
		LogUtils.getLogger().info("");
		
		try {
//			@SuppressWarnings("resource")
//			ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

			LogUtils.getLogger().info("Service");
//			InterfaceMetricsService service = (InterfaceMetricsService) ctx.getBean("interfaceMetricsService");
			InterfaceMetricsService service = new InterfaceMetricsService();
			service.execute();

		} catch (Exception e) {
			LogUtils.getLogger().error(e);
			e.printStackTrace();
		}

		LogUtils.getLogger().info("");
		LogUtils.getLogger().info("Aplicativo finalizado. Tempo de execucao: " + getDuractionTime());
		LogUtils.getLogger().info("");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void setStartTime(){
		startTime = System.currentTimeMillis();
	}

	private static String getDuractionTime(){
		long duraction = System.currentTimeMillis() - startTime;
		
		duraction /= 1000;
		if(duraction < 60)
			return duraction + " segundos.";
		
		duraction /= 60;
		return duraction + " minutos.";
	}
}