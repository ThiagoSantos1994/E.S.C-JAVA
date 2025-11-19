SELECT
	CASE
        WHEN ds_Mes = '' THEN 'ERRO'
        ELSE
          CASE ds_Mes
            WHEN '01' THEN 'Jan'
            WHEN '02' THEN 'Fev'
            WHEN '03' THEN 'Mar'
            WHEN '04' THEN 'Abr'
            WHEN '05' THEN 'Mai'
            WHEN '06' THEN 'Jun'
            WHEN '07' THEN 'Jul'
            WHEN '08' THEN 'Ago'
            WHEN '09' THEN 'Set'
            WHEN '10' THEN 'Out'
            WHEN '11' THEN 'Nov'
            WHEN '12' THEN 'Dez'
            ELSE 'Mês inválido'
          END + ' ' + ds_Ano
      END AS MesPorExtenso
FROM
    tbd_DespesasFixasMensais
WHERE
    id_Despesa = :idDespesa
    AND id_Funcionario = :idFuncionario