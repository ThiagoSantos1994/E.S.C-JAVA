SELECT DISTINCT
    id_DespesaParcelada,
    ds_TituloDespesaParcelada
FROM
    tbd_DespesasParceladas
WHERE
    id_Funcionario = :idFuncionario
    AND id_DespesaParcelada NOT IN (SELECT DISTINCT id_DespesaParcelada
                                    FROM tbd_DetalheDespesasMensais
                                    WHERE id_DespesaParcelada > 0
                                    AND id_DespesaParcelada IS NOT NULL
                                    AND id_Funcionario = :idFuncionario)
